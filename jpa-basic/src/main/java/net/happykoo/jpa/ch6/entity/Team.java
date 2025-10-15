package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "TeamV2")
@Table(name = "V2_TEAM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Team {
    @Id
    @Column(name = "TEAM_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    //1:N 단일 관계
    //JoinColumn 을 사용하지 않으면 조인테이블로 FK 관리
    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> memberList;
}
