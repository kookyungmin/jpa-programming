package net.happykoo.jpa.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.annotation.Inherited;
import java.util.List;

@Entity
@Table(name = "ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "D_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories;
}
