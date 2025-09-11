package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.dto.ProductUpdateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    //상품수정
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody @Valid ProductUpdateRequest productUpdateRequest){
        Product updateProduct = productService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok().body(updateProduct);
    }

    //상품삭제
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId){
       Product deleteProduct = productService.deleteProduct(productId);
        return ResponseEntity.ok(deleteProduct);
    }
}
