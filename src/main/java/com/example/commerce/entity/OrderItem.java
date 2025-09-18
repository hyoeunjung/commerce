package com.example.commerce.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int productPrice;
    @Column(nullable = false)
    private int quantity;

    public int calculateItemAmount() {
        return this.productPrice * this.quantity;
    }
}