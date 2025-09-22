package com.example.commerce.controller;

import com.example.commerce.dto.OrderCreateRequest;
import com.example.commerce.dto.OrderResponse;
import com.example.commerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal String userEmail,
            @Valid @RequestBody OrderCreateRequest orderCreateRequest) {

        OrderResponse orderResponse = orderService.createOrder(userEmail, orderCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    //조회
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
                                                          @AuthenticationPrincipal String userEmail,
                                                          Pageable pageable) {

        Page<OrderResponse> ordersPage = orderService.getOrdersByUser(userEmail, pageable);
        return ResponseEntity.ok(ordersPage);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @AuthenticationPrincipal String userEmail,
            @PathVariable Long orderId){
        OrderResponse orderResponse = orderService.getOrderDetails(userEmail, orderId);
        return ResponseEntity.ok(orderResponse);
    }

}
