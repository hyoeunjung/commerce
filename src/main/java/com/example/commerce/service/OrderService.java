package com.example.commerce.service;

import com.example.commerce.dto.OrderCreateRequest;
import com.example.commerce.dto.OrderResponse;
import com.example.commerce.entity.*;
import com.example.commerce.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final UserService userService;

    @Transactional
    public OrderResponse createOrder(String userEmail, OrderCreateRequest orderCreateRequest) {

        User user = userService.getUserByEmail(userEmail);

        Cart userCart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("사용자의 장바구니를 찾을 수 없습니다."));

        List<CartItem> cartItems = userCart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 상품이 없습니다. 주문할 상품을 추가해주세요.");
        }

        Order newOrder = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod(orderCreateRequest.getPaymentMethod())
                .recipientName(orderCreateRequest.getRecipientName())
                .recipientPhone(orderCreateRequest.getRecipientPhone())
                .deliveryAddress(orderCreateRequest.getRecipientAddress())
                .build();

        int totalOrderAmount = 0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();


            product.decreaseStock(cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();

            newOrder.addOrderItem(orderItem);
            totalOrderAmount += orderItem.calculateItemAmount();
        }

        newOrder.setTotalAmount(totalOrderAmount);

        Order savedOrder = orderRepository.save(newOrder);

        cartItemRepository.deleteAll(cartItems);
        userCart.getCartItems().clear();

        return new OrderResponse(savedOrder);
    }

}
