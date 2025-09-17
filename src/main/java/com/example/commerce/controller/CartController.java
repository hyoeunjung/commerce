package com.example.commerce.controller;

import com.example.commerce.dto.CartItemAddRequest;
import com.example.commerce.dto.CartItemResponse;
import com.example.commerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addCartItem(
            @AuthenticationPrincipal String userEmail,
            @Valid @RequestBody CartItemAddRequest cartItemAddRequest){

        CartItemResponse cartItemResponse = cartService.addCartItem(userEmail, cartItemAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }
}
