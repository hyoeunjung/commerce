package com.example.commerce.repository;

import com.example.commerce.entity.Cart;
import com.example.commerce.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);


}
