package net.happykoo.jpa.example.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Item {
    private String author;
    private String isbn;
}
