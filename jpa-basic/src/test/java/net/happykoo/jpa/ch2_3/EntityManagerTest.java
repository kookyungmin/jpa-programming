package net.happykoo.jpa.ch2_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import net.happykoo.jpa.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class EntityManagerTest {
    @Test
    @DisplayName("EntityManger 예제 테스트")
    public void test1() {
        transactionTemplate(em -> runEntityCRUD(em));
    }

    @Test
    @DisplayName("detach / merge 예제")
    public void test2() {
        transactionTemplate(em -> runEntityDetachAndMerge(em));
    }

    private void transactionTemplate(Consumer<EntityManager> fn) {
        //Persistence Context 장점
        //1. 1차 캐시 -> Entity 들을 @Id 기반으로 1차 캐시로 관리하여 find 로 데이터를 찾을 때, 1차캐시에서 먼저 찾은 후 없으면 데이터베이스에서 조회한다.
        //2. 트랜잭션 쓰기 지연 -> commit 전까지의 SQL 를 모아놨다가 커밋할 때 모아둔 쿼리를 데이터베이스에 보냄(flush: 데이터베이스와 동기화)
        //3. 변경 감지 -> 엔티티의 변경사항을 데이터베이스에 자동으로 반영 -> 엔티티의 최초상태를 스냅샷으로 저장 -> flush 시점에 스냅샷과 엔티티를 비교해서 변경된 엔티티 찾음
        //위의 모든 작업은 EntityManager 의 생명주기 동안에만 유지된다.

        EntityTransaction tx = null;
        //엔티티 매니저 팩토리 생성 (원래 엔티티 매니저 팩토리는 생성 비용이 크기에 싱글턴으로 관리)
        //엔티티 매니저 생성 -> 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있음 -> 스레드 간 공유해서는 안됨
        try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("happykoo");
            EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();

            tx.begin();
            //메인 로직(모두 1차 캐시되고, 트랜잭션 쓰기 지연이 되어 commit 전까지는 Persistence Context 에 저장된다)
            fn.accept(em);
            //commit 후에 비로소 데이터베이스로 SQL 실행
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    private void runEntityCRUD(EntityManager em) {
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


        //목록 조회(JPQL[Java Persistence Query Language] 사용) -> JPQL 사용 시 자동 flush
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        assertEquals(1, members.size());

        //삭제
        em.remove(member);

        assertEquals(0, em.createQuery("select m from Member m", Member.class).getResultList().size());
    }

    private void runEntityDetachAndMerge(EntityManager em) {
        Member member = Member.builder()
                .id("happykoo")
                .username("해피쿠")
                .age(33)
                .build();
        em.persist(member);

        assertTrue(em.contains(member));

        //member 는 준영속 상태가 되고 persistence context 에서 제거
        em.detach(member);

        assertFalse(em.contains(member));

        //merge 하면, persistence context에 등록되는데, 이 때, 기존에 있던 member와는 다른 인스턴스이다.
        Member mergeMember = em.merge(member);

        assertTrue(em.contains(mergeMember));
        assertFalse(member == mergeMember);
    }
}
