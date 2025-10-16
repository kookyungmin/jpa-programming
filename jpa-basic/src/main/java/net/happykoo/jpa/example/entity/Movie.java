package net.happykoo.jpa.example.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("M")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends Item {
    private String director;
    private String actor;
}
