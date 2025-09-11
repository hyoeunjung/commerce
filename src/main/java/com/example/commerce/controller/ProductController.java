package com.example.commerce.controller;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.dto.ProductResponse;
import com.example.commerce.dto.ProductUpdateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    //상품등록
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        Product createdProduct = productService.createProduct(productCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(createdProduct));
    }

    //상품수정
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
                                                         @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        Product updatedProduct = productService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok().body(new ProductResponse(updatedProduct));
    }
<<<<<<< HEAD

    //상품삭제
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId){
       Product deleteProduct = productService.deleteProduct(productId);
        return ResponseEntity.ok(deleteProduct);
    }
}
=======
}
>>>>>>> b3a4ae02b39857e619eef45a33f89b48b6551966
