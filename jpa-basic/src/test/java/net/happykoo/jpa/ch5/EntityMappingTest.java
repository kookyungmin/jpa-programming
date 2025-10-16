package net.happykoo.jpa.ch5;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch5.entity.Member;
import net.happykoo.jpa.ch5.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityMappingTest {
    private PersistenceContextHandler pc;

    @BeforeEach
    public void setup() {
        pc = new PersistenceContextHandler("happykoo");
        pc.runTransaction((em, tx) -> {
            Team team = Team.builder()
                    .id("happyteam")
                    .name("해피팀")
                    .members(new ArrayList<>())
                    .build();
            em.persist(team);

            Member m1 = Member.builder()
                    .id("happykoo")
                    .userName("해피쿠")
                    .team(team)
                    .build();
            Member m2 = Member.builder()
                    .id("marco")
                    .userName("마르코")
                    .team(team)
                    .build();

            em.persist(m1);
            em.persist(m2);
        });

    }

    @Test
    @DisplayName("연관관계 테스트 : CRUD")
    public void test1() {
        pc.runTransaction((em, tx) -> {
            //객체 그래프 탐색
            Member findMember1 = em.find(Member.class, "happykoo");
            assertEquals("happyteam", findMember1.getTeam().getId());

            //JPQL(Java Persistence Query Language) 사용
            String selectQuery = "select m from MemberV3 m join m.team t where t.id=:teamId";

            assertEquals(2, em.createQuery(selectQuery)
                    .setParameter("teamId", "happyteam")
                    .getResultList().size());

            //새로운 팀2 생성, m1 팀 변경
            Team team2 = Team.builder()
                    .id("koo")
                    .name("Koo")
                    .members(new ArrayList<>())
                    .build();
            em.persist(team2);
            findMember1.setTeam(team2);

            assertEquals(1, em.createQuery(selectQuery)
                    .setParameter("teamId", "happyteam")
                    .getResultList().size());

            //연관관계 먼저 제거하지 않으면, 외래키 제약 위반 예외 발생
            findMember1.setTeam(null);
            em.remove(team2);
        });
    }

    @Test
    @DisplayName("연관관계 테스트 : 양방향 연관관계")
    public void test2() {
        pc.runTransaction((em, tx) -> {
            Team findTeam = em.find(Team.class, "happyteam");
            assertEquals(2, findTeam.getMembers().size());
        });
    }
}
