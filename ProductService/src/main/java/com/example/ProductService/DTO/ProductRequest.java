package com.example.ProductService.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    String productName;
    BigDecimal price;
    LocalDate mfdDate;
    int quantity;
}
