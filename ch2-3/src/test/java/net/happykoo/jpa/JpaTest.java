package net.happykoo.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import net.happykoo.jpa.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaTest {
    @Test
    @DisplayName("EntityManger 예제 테스트")
    public void test1() {
        EntityTransaction tx = null;
        //엔티티 매니저 팩토리 생성 (원래 엔티티 매니저 팩토리는 생성 비용이 크기에 싱글턴으로 관리)
        //엔티티 매니저 생성 -> 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있음 -> 스레드 간 공유해서는 안됨
        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("happykoo");
            EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();

            tx.begin();

            //메인 로직
            logic(em);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    private void logic(EntityManager em) {
        Member member = Member.builder()
                .id("happykoo")
                .username("해피쿠")
                .age(33)
                .build();

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //한 건 조회
        Member findMember = em.find(Member.class, member.getId());

        assertEquals(member.getId(), findMember.getId());
        assertEquals(20, findMember.getAge());
        assertTrue(member == findMember);


        //목록 조회(JPQL[Java Persistence Query Language] 사용)
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        assertEquals(1, members.size());

        //삭제
        em.remove(member);

        assertEquals(0, em.createQuery("select m from Member m", Member.class).getResultList().size());
    }
}
