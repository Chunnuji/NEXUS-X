package com.example.ProductService.Service;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.DTO.ProductResponse;
import com.example.ProductService.Dao.ProductRepository;
import com.example.ProductService.Entity.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        product.setUpdateDate(LocalDate.now());
        product.setCreateDate(LocalDate.now());
        product.setStatus(true);
        product = productRepository.save(product);
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow();
        BeanUtils.copyProperties(productRequest, product);
        product.setUpdateDate(LocalDate.now());
        product = productRepository.save(product);
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getById(Integer id) {
        Product product= productRepository.getById(id);
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public List<ProductResponse> getAll() {
        List<Product> productList = productRepository.findAll();

        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product p: productList){
            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(p,productResponse);
            productResponseList.add(productResponse);
        }
        return productResponseList;
    }
}
