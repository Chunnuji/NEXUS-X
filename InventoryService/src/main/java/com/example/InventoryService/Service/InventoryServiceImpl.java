package com.example.InventoryService.Service;


import com.example.InventoryService.DTO.InventoryRequest;
import com.example.InventoryService.DTO.InventoryStatus;
import com.example.InventoryService.Dao.InventoryRepository;
import com.example.InventoryService.Dao.InventoryResevationRepository;
import com.example.InventoryService.Entity.Inventory;
import com.example.InventoryService.Entity.InventoryReservation;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryResevationRepository reservationRepository;

    @Autowired
    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            InventoryResevationRepository reservationRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public InventoryStatus getInventoryStatus(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        InventoryStatus inventoryStatus = new InventoryStatus();
        BeanUtils.copyProperties(inventory, inventoryStatus);
        return inventoryStatus;
    }

    @Override
    public InventoryStatus reserveStock(InventoryRequest request) {

        // Idempotency check
        Optional<InventoryReservation> existingReservation =
                reservationRepository.findByOrderId(request.getOrderId());

        if (existingReservation.isPresent()) {
            InventoryReservation reservation = existingReservation.get();

            // Already reserved, just return current inventory status
            Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
            InventoryStatus inventoryStatus = new InventoryStatus();
            BeanUtils.copyProperties(inventory, inventoryStatus);
            return inventoryStatus;
        }

        // Check stock availability
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());

        if (request.getQuantity() > inventory.getAvailableQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Update inventory totals
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());
        inventoryRepository.save(inventory);

        // Create reservation record
        InventoryReservation reservation = new InventoryReservation();
        reservation.setOrderId(request.getOrderId());
        reservation.setProductId(request.getProductId());
        reservation.setQuantity(request.getQuantity());
        reservation.setStatus(InventoryReservation.ReservationStatus.RESERVED);
        reservationRepository.save(reservation);

        InventoryStatus inventoryStatus = new InventoryStatus();
        BeanUtils.copyProperties(inventory, inventoryStatus);
        return inventoryStatus;
    }

    @Override
    public InventoryStatus releaseStock(InventoryRequest request) {

        Optional<InventoryReservation> reservationOpt =
                reservationRepository.findByOrderId(request.getOrderId());

        if (reservationOpt.isEmpty()) {
            // No reservation exists — safe to ignore
            Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
            InventoryStatus inventoryStatus = new InventoryStatus();
            BeanUtils.copyProperties(inventory, inventoryStatus);
            return inventoryStatus;
        }

        InventoryReservation reservation = reservationOpt.get();

        // Already released, do nothing
        if (reservation.getStatus() == InventoryReservation.ReservationStatus.RELEASED) {
            return getInventoryStatus(request.getProductId());
        }

        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());

        // Restore quantities
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservation.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() - reservation.getQuantity());
        inventoryRepository.save(inventory);

        // Update reservation status
        reservation.setStatus(InventoryReservation.ReservationStatus.RELEASED);
        reservationRepository.save(reservation);

        InventoryStatus inventoryStatus = new InventoryStatus();
        BeanUtils.copyProperties(inventory, inventoryStatus);
        return inventoryStatus;
    }
}