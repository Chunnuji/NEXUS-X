package com.example.ProductService.Controller;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.DTO.ProductResponse;
import com.example.ProductService.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.create(productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.update(id, productRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Integer id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> responses = productService.getAll();
        return ResponseEntity.ok(responses);
    }
}
