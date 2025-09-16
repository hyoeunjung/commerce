package com.example.commerce.repository;

import com.example.commerce.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findbyCartInAndProductId(Long cartId, Long productId);
}
