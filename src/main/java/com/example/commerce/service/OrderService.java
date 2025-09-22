package com.example.commerce.service;

import com.example.commerce.dto.OrderCreateRequest;
import com.example.commerce.dto.OrderResponse;
import com.example.commerce.entity.*;
import com.example.commerce.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Optional 임포트

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
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod(orderCreateRequest.getPaymentMethod())
                .recipientName(orderCreateRequest.getRecipientName())
                .recipientPhone(orderCreateRequest.getRecipientPhone())
                .deliveryAddress(orderCreateRequest.getRecipientAddress())
                .build();

        int totalOrderAmount = 0;

        for (CartItem cartItem : cartItems) {

            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + cartItem.getProduct().getId()));

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

        // 장바구니 아이템 삭제
        cartItemRepository.deleteAll(cartItems);
        userCart.getCartItems().clear();


        return new OrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUser(String userEmail, Pageable pageable){
        User user = userService.getUserByEmail(userEmail);

        Page<Order> ordersPage = orderRepository.findByUser(user, pageable);
        return ordersPage.map(OrderResponse::new);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderDetails(String userEmail, Long orderId){
        User user = userService.getUserByEmail(userEmail);

        Order order = orderRepository.findByIdAndUserWithOrderItems(orderId, user)
                .orElseThrow(() -> new EntityNotFoundException("해당 주문을 찾을 수 없음"));
        return new OrderResponse(order);
    }
}