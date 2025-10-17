package net.happykoo.jpa.ch10.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ProductV2")
@Table(name = "V2_PRODUCT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockAmount;
}
