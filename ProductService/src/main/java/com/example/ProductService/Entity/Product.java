package com.example.ProductService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PRODUCT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "PRODUCT_NAME", unique = true, nullable = false)
    String productName;

    @Column(name = "PRICE", nullable = false)
    BigDecimal price;

    @Column(name = "MFD_DATE", nullable = false)
    LocalDate mfdDate;

    @Column(name = "QUANTITY", nullable = false)
    int quantity;

    @Column(name = "STATUS", nullable = false)
    boolean status;

    @Column(name = "CREATE_DATE", nullable = false)
    LocalDate createDate;

    @Column(name ="UPDATE_DATE", nullable = false)
    LocalDate updateDate;

}
