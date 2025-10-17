package net.happykoo.jpa.example.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
