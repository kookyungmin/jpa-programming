package net.happykoo.jpa.example.entity;

import jakarta.persistence.*;
import lombok.*;
import net.happykoo.jpa.ch7.entity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
