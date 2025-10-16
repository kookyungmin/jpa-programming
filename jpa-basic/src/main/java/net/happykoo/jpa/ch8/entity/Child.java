package net.happykoo.jpa.ch8.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Child {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;
}
