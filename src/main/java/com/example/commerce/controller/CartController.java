package com.example.commerce.controller;

import com.example.commerce.dto.CartItemAddRequest;
import com.example.commerce.dto.CartItemResponse;
import com.example.commerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //담기
    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addCartItem(
            @AuthenticationPrincipal String userEmail,
            @Valid @RequestBody CartItemAddRequest cartItemAddRequest){

        Long userId = cartService.getUserIdByEmail(userEmail);
        CartItemResponse cartItemResponse = cartService.addCartItem(userId, cartItemAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }

    //조회
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(
            @AuthenticationPrincipal String userEmail){

        Long userId = cartService.getUserIdByEmail(userEmail);
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
}

