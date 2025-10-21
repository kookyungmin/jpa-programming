package net.happykoo.jpashop.domain;

import lombok.*;
import net.happykoo.jpashop.constant.DeliveryStatus;
import net.happykoo.jpashop.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {
    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    @Builder
    public Order(Member member,
                 Delivery delivery,
                 List<OrderItem> orderItems) {
        this.member = member;
        this.status = OrderStatus.ORDER;
        this.orderDate = LocalDateTime.now();
        this.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            this.addOrderItem(orderItem);
        }
    }

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new RuntimeException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.status = OrderStatus.CANCEL;
        this.orderItems.forEach(item -> item.cancel());
    }

    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
