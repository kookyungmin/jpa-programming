package net.happykoo.jpa.ch8;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch8.entity.Member;
import net.happykoo.jpa.ch8.entity.Team;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyTest {
    @Test
    @DisplayName("Proxy 테스트 :: getReference")
    public void test1() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Team t1 = Team.builder()
                        .name("happyteam")
                        .build();

                Member m1 = Member.builder()
                        .name("happykoo")
                        .team(t1)
                        .build();

                em.persist(t1);
                em.persist(m1);
                tx.commit();
                em.clear();

                //getReference 호출하면 SQL 실행 X 프록시 객체 생성(프록시 클래스는 실제 클래스를 상속받음)
                Member findMember = em.getReference(Member.class, m1.getId());
                assertFalse(Hibernate.isInitialized(findMember));

                //식별자는 이미 가지고 있으므로 Id 를 조회해도 DB에서 조회 X
                assertEquals(m1.getId(), findMember.getId());
                assertFalse(Hibernate.isInitialized(findMember));

                //지연 로딩: 실제 데이터 사용 시 DB 에서 조회 -> Persistence Context에 이미 있으면 DB 조회 X
                assertEquals(m1.getName(), findMember.getName());
                assertTrue(Hibernate.isInitialized(findMember));
            });
        }
    }

    @Test
    @DisplayName("Lazy Loading 테스트")
    public void test2() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Team t1 = Team.builder()
                        .name("happyteam")
                        .build();

                Member m1 = Member.builder()
                        .name("happykoo")
                        .team(t1)
                        .build();

                em.persist(t1);
                em.persist(m1);
                tx.commit();
                em.clear();

                Member findMember = em.find(Member.class, m1.getId());
                assertFalse(Hibernate.isInitialized(findMember.getTeam()));

                //Team 사용 시 SQL 실행
                assertEquals(t1.getName(), findMember.getTeam().getName());
                assertTrue(Hibernate.isInitialized(findMember.getTeam()));
            });
        }

    }
}
