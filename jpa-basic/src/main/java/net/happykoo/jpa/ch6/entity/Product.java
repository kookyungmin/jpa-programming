package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "ProductV1")
@Table(name = "V1_PRODUCT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
