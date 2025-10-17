package net.happykoo.jpa.ch10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.jpa.ch10.embedded.Address;

@Entity(name = "OrderV2")
@Table(name = "V2_ORDER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order {
    @Id @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
