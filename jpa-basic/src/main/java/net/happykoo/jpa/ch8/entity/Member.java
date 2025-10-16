package net.happykoo.jpa.ch8.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "MemberV5")
@Table(name = "V5_MEMBER")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String name;

//    @ManyToOne, @OneToOne : 디폴트 EAGER
//    @OneToMany, @ManyToMany : 디폴트 LAZY

//    @ManyToOne(fetch = FetchType.EAGER) //즉시로딩 -> 조인 쿼리 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false) //nullable false 하면, inner join으로 가져옴
    private Team team;
}
