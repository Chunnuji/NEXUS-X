package com.example.OrderService.Service;

import com.example.OrderService.DTO.InventoryRequest;
import com.example.OrderService.DTO.InventoryStatus;
import com.example.OrderService.Dao.InventoryRepository;
import com.example.OrderService.Dao.InventoryResevationRepository;
import com.example.OrderService.Entity.Inventory;
import com.example.OrderService.Entity.InventoryReservation;
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

        // 1️⃣ Idempotency check
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

        // 2️⃣ Check stock availability
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());

        if (request.getQuantity() > inventory.getAvailableQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 3️⃣ Update inventory totals
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());
        inventoryRepository.save(inventory);

        // 4️⃣ Create reservation record
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

        // 1️⃣ Restore quantities
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservation.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() - reservation.getQuantity());
        inventoryRepository.save(inventory);

        // 2️⃣ Update reservation status
        reservation.setStatus(InventoryReservation.ReservationStatus.RELEASED);
        reservationRepository.save(reservation);

        InventoryStatus inventoryStatus = new InventoryStatus();
        BeanUtils.copyProperties(inventory, inventoryStatus);
        return inventoryStatus;
    }
}
//    @Override
//    public InventoryStatus reserveStock(InventoryRequest request) {
//
//        // 1️⃣ Idempotency check
//        reservationRepository.findByOrderId(request.getOrderId())
//                .ifPresent(r -> {
//                    throw new RuntimeException("Stock already reserved for order");
//                });
//
//        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
//
//        if (request.getQuantity() > inventory.getAvailableQuantity()) {
//            throw new RuntimeException("Insufficient stock");
//        }
//
//        // 2️⃣ Update inventory totals
//        inventory.setAvailableQuantity(
//                inventory.getAvailableQuantity() - request.getQuantity()
//        );
//        inventory.setReservedQuantity(
//                inventory.getReservedQuantity() + request.getQuantity()
//        );
//
//        inventoryRepository.save(inventory);
//
//        // 3️⃣ Create reservation record
//        InventoryReservation reservation = new InventoryReservation();
//        reservation.setOrderId(request.getOrderId());
//        reservation.setProductId(request.getProductId());
//        reservation.setQuantity(request.getQuantity());
//        reservation.setStatus(InventoryReservation.ReservationStatus.RESERVED);
//
//        reservationRepository.save(reservation);
//
//        InventoryStatus inventoryStatus = new InventoryStatus();
//        BeanUtils.copyProperties(inventory, inventoryStatus);
//        return inventoryStatus;
//    }
//
//    @Override
//    public InventoryStatus releaseStock(InventoryRequest request) {
//
//        InventoryReservation reservation = reservationRepository
//                .findByOrderId(request.getOrderId())
//                .orElseThrow(() -> new RuntimeException("No reservation found"));
//
//        if (reservation.getStatus() == InventoryReservation.ReservationStatus.RELEASED) {
//            return getInventoryStatus(request.getProductId());
//        }
//
//        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
//
//        // 1️⃣ Restore quantities
//        inventory.setAvailableQuantity(
//                inventory.getAvailableQuantity() + reservation.getQuantity()
//        );
//        inventory.setReservedQuantity(
//                inventory.getReservedQuantity() - reservation.getQuantity()
//        );
//
//        inventoryRepository.save(inventory);
//
//        // 2️⃣ Update reservation status
//        reservation.setStatus(InventoryReservation.ReservationStatus.RELEASED);
//        reservationRepository.save(reservation);
//
//        InventoryStatus inventoryStatus = new InventoryStatus();
//        BeanUtils.copyProperties(inventory, inventoryStatus);
//        return inventoryStatus;
//    }
//}


//@Service
//public class InventoryServiceImpl implements InventoryService{
//
//    private final InventoryRepository inventoryRepository;
//
//    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
//        this.inventoryRepository = inventoryRepository;
//    }
//
//    @Override
//    public InventoryStatus getInventoryStatus(Long productId) {
//        Inventory inventory = inventoryRepository.findByProductId(productId);
//        InventoryStatus inventoryStatus = new InventoryStatus();
//        BeanUtils.copyProperties(inventory,inventoryStatus);
//        return inventoryStatus;
//    }
//
//    @Override
//    public InventoryStatus reserveStock(InventoryRequest request) {
//        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
//        if(request.getQuantity()<=inventory.getAvailableQuantity()){
//            Integer reservedQuantity = inventory.getReservedQuantity()+request.getQuantity();
//            inventory.setReservedQuantity(reservedQuantity);
//            Integer availQuantity = inventory.getAvailableQuantity()-request.getQuantity();
//            inventory.setAvailableQuantity(availQuantity);
//        }
//        Inventory inventory1 = inventoryRepository.save(inventory);
//        InventoryStatus inventoryStatus = new InventoryStatus();
//        BeanUtils.copyProperties(inventory1,inventoryStatus);
//        return inventoryStatus;
//    }
//
//    @Override
//    public InventoryStatus releaseStock(InventoryRequest request) {
//        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
//        Integer reservedQuantity = inventory.getReservedQuantity()-request.getQuantity();
//        inventory.setReservedQuantity(reservedQuantity);
//        Integer availQuantity = inventory.getAvailableQuantity()+request.getQuantity();
//        inventory.setAvailableQuantity(availQuantity);
//        Inventory inventory1 = inventoryRepository.save(inventory);
//        InventoryStatus inventoryStatus = new InventoryStatus();
//        BeanUtils.copyProperties(inventory1,inventoryStatus);
//        return inventoryStatus;
//    }
//}
