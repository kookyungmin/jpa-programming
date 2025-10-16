package net.happykoo.jpa.example.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("A")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Album extends Item {
    private String artist;
    private String etc;
}
