package net.happykoo.jpa.ch8.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TeamV3")
@Table(name = "V3_TEAM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String name;
}
