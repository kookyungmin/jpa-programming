package net.happykoo.jpa.ch7.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.happykoo.jpa.ch7.constant.ChickenGender;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DiscriminatorValue("C")
public class Chicken extends Food {
    @Enumerated(EnumType.STRING)
    private ChickenGender gender;
}
