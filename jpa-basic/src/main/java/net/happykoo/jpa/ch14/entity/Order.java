package net.happykoo.jpa.ch14.entity;

import jakarta.persistence.*;

@Entity(name = "OrderV3")
@Table(name = "V3_ORDER")
public class Order {
    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Child child;
}
