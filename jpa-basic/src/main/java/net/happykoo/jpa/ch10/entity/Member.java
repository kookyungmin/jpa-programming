package net.happykoo.jpa.ch10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "MemberV7")
@NamedQuery(name = "MemberV7.findByUsername", query = "SELECT m FROM MemberV7 m WHERE m.username = :username")
@Table(name = "V7_MEMBER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;
}
