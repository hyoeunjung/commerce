package com.example.commerce.service;

import com.example.commerce.dto.ProductCreateRequest;
import com.example.commerce.dto.ProductUpdateRequest;
import com.example.commerce.entity.Product;
import com.example.commerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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

    //상품수정
    @Transactional
    public Product updateProduct(Long productId, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 상품을 찾을수 없음: " + productId));

        product.update(productUpdateRequest.getName(),
                       productUpdateRequest.getPrice(),
                        productUpdateRequest.getStock(),
                        productUpdateRequest.getDescription());
        return product;

    }
}
