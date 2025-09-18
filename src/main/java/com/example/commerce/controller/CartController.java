package com.example.commerce.controller;

import com.example.commerce.dto.CartItemAddRequest;
import com.example.commerce.dto.CartItemResponse;
import com.example.commerce.dto.CartItemUpdateRequest;
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

        CartItemResponse cartItemResponse = cartService.addCartItem(userEmail, cartItemAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }

    //조회
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(
            @AuthenticationPrincipal String userEmail){

        List<CartItemResponse> cartItems = cartService.getCartItems(userEmail);
        return ResponseEntity.ok(cartItems);
    }

    //수량수정
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest,
            @AuthenticationPrincipal String userEmail){


        CartItemResponse updatedItem = cartService.updateCartItemQuantity(
                userEmail,
                productId,
                cartItemUpdateRequest.getQuantity()
        );

        return ResponseEntity.ok(updatedItem);

    }

    //삭제
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal String userEmail) {


        cartService.deleteCartItem(userEmail, productId);

        return ResponseEntity.noContent().build();
    }


}

