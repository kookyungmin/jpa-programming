package net.happykoo.jpa.ch10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "TeamV4")
@Table(name = "V4_TEAM")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members;

}
