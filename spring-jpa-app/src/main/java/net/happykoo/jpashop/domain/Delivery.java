package net.happykoo.jpashop.domain;

import lombok.*;
import net.happykoo.jpashop.constant.DeliveryStatus;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    @Setter
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus status;

    @Builder
    public Delivery(Address address) {
        this.address = address;
        this.status = DeliveryStatus.READY;
    }
}
