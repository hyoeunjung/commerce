package com.example.commerce.dto;

import com.example.commerce.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private int price;
    private int totalPrice;

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getProduct().getPrice();
        this.totalPrice = this.price * this.quantity;
    }
}
