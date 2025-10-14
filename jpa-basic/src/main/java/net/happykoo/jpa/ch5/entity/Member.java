package net.happykoo.jpa.ch5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity(name = "MemberV3")
@Table(name = "V3_MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String userName;
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeam(Team team) {
        //기존 team 에서 삭제
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        //양방향 관계를 맺을 때, 한쪽만 객체에만 셋팅하는 것보다 양쪽다 셋팅하는 것이 안전(순수 객체 상태에서 정상적으로 동작하기 위함)
        //단, 양방향 관계 지양해야함 (양쪽 관계에 의해 관리포인트 늘어나게 되고, N+1 문제 발생)
        Optional.ofNullable(team)
                .ifPresent(t -> t.getMembers().add(this));
    }
}
