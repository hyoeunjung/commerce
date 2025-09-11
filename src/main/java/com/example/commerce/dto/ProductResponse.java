package com.example.commerce.dto;

import com.example.commerce.entity.Product;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private boolean isDeleted;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.description = product.getDescription();
        this.isDeleted = product.isDeleted();
        this.createdDate = product.getCreatedDate();
        this.lastModifiedDate = product.getLastModifiedDate();
    }
}