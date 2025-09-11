package com.example.commerce.dto;

import com.example.commerce.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.description = product.getDescription();
    }
}
