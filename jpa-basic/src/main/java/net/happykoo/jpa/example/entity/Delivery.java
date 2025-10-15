package net.happykoo.jpa.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.happykoo.jpa.example.constant.DeliveryStatus;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @Column(name = "DELIVERY_ID")
    private Long id;
    private String city;
    private String street;
    private String zipCode;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
