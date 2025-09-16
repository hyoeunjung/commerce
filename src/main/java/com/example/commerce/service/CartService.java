package com.example.commerce.service;


import com.example.commerce.dto.CartItemAddRequest;
import com.example.commerce.dto.CartItemResponse;
import com.example.commerce.entity.Cart;
import com.example.commerce.entity.CartItem;
import com.example.commerce.entity.Product;
import com.example.commerce.entity.User;
import com.example.commerce.repository.CartItemRepository;
import com.example.commerce.repository.CartRepository;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long getUserIdByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을수 없음"));
        return user.getId();
    }

    @Transactional
    public CartItemResponse addCartItem(Long userId, CartItemAddRequest cartItemAddRequest) {

        //사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을수 없음"));

        //사용자 장바구니를 조회하고 없으면 새로 생성
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        //상품 조회
        Product product = productRepository.findById(cartItemAddRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을수없음"));

        //상품 검증
        if (product.isDeleted()) {
            throw new IllegalArgumentException("삭제된 상품은 담을수 없음");
        }
        if (cartItemAddRequest.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("상품 재고가 부족함");
        }

        //CartItem 조회하고 이미 담겼으면 수량을 누적
        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, product.getId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        //수량을 누적
        cartItem.setQuantity(cartItem.getQuantity() +cartItemAddRequest.getQuantity());

        cartItemRepository.save(cartItem);

        return new CartItemResponse(cartItem);
    }

    //장바구니 전체 조회
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("장바구니가 존재하지 않음"));

        return cart.getCartItems().stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
    }
}
