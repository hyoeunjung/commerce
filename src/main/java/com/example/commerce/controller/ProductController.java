package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //상품등록
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        Product createProduct = productService.createProduct(productCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }

}
