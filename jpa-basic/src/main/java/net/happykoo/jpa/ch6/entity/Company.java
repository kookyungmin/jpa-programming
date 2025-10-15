package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @Column(name = "COMPANY_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
}
