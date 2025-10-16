package net.happykoo.jpa.example.entity;

import jakarta.persistence.*;
import lombok.*;
import net.happykoo.jpa.ch7.entity.BaseEntity;
import net.happykoo.jpa.example.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "ORDER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    //연관 관계 메서드
    public void setUser(User user) {
        Optional.ofNullable(this.user)
                .map(User::getOrders)
                .ifPresent(orders -> orders.remove(this));
        this.user = user;
        Optional.ofNullable(user)
                .map(User::getOrders)
                .ifPresent(orders -> orders.add(this));
    }
}
