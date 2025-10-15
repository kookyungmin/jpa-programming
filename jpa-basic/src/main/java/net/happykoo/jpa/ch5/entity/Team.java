package net.happykoo.jpa.ch5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "TeamV1")
@Table(name = "V1_TEAM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Team {
    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    //양방향의 경우 mappedBy 반대쪽 매핑 필드 명을 적으면 됨 (연관관계의 주인을 지정), mappedBy 는 연관관계 주인인지 아닌지 판단하는 근거이며, 주인은 mappedBy 속성을 사용하지 않음
    //연관관계 주인은 테이블에 외래 키가 있는 곳으로 정해야함 (Member 테이블에 외래키(team_id)가 존재하므로 연관관계 주인은 Member 엔티티의 team)
    //members 에 값을 아무리 추가해도 저장될 때 외래키가 생성되지 않음
    //단, OneToMany 지양 (단방향인 경우, member 수만큼 update 쿼리가 많이 날라가고, N+1 문제가 발생, 양방향인 경우도 양쪽 관계에 의해 관리포인트 늘어나게 되고, N+1 문제 발생)
    @OneToMany(mappedBy = "team")
    private List<Member> members;
}
