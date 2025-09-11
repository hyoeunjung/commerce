package com.example.commerce.service;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(ProductCreateRequest productCreateRequest) {
        Product product = Product.builder()
                .name(productCreateRequest.getName())
                .price(productCreateRequest.getPrice())
                .stock(productCreateRequest.getStock())
                .description(productCreateRequest.getDescription())
                .build();

        return productRepository.save(product);
    }
}
