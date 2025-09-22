package com.example.commerce.dto;

import com.example.commerce.entity.Order;
import com.example.commerce.entity.OrderStatus;
import com.example.commerce.entity.PaymentMethod;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {
    private Long orderId;
    private String userEmail;
    private int totalAmount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
    private LocalDateTime orderDate;

    private List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userEmail= order.getUser().getEmail();
        this.totalAmount = order.getTotalAmount();
        this.orderStatus = order.getOrderStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.recipientName = order.getRecipientName();
        this.recipientPhone = order.getRecipientPhone();
        this.deliveryAddress = order.getDeliveryAddress();
        this.orderDate = order.getOrderDate();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }
}
