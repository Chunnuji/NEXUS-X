package com.example.ProductService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    int id;
    String productName;
    BigDecimal price;
    LocalDate mfdDate;
    int quantity;
    boolean status;
    LocalDate createDate;
    LocalDate updateDate;
}
