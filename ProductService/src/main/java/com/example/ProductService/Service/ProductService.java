package com.example.ProductService.Service;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.DTO.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest product);
    ProductResponse update(Integer id, ProductRequest product);
    void delete(Integer id);
    ProductResponse getById(Integer id);
    List<ProductResponse> getAll();
}
