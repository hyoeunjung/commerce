package com.example.commerce.dto;

import com.example.commerce.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private int productPrice;
    private int quantity;
    private int itemAmount;

    public OrderItemResponse(OrderItem orderItem) {

        this.productId = orderItem.getProductId();
        this.productName = orderItem.getProductName();
        this.productPrice = orderItem.getProductPrice();
        this.quantity = orderItem.getQuantity();
        this.itemAmount = orderItem.calculateItemAmount();
    }
}
