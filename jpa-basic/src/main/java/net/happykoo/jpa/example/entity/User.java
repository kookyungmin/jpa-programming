package net.happykoo.jpa.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String city;

    private String street;

    private String zipCode;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
