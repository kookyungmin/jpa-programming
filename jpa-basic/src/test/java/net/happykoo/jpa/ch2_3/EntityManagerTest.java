package net.happykoo.jpa.ch2_3;

import jakarta.persistence.EntityManager;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch2_3.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityManagerTest {
    @Test
    @DisplayName("EntityManger 예제 테스트")
    public void test1() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> runEntityCRUD(em));
        }
    }

    @Test
    @DisplayName("detach / merge 예제")
    public void test2() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> runEntityDetachAndMerge(em));
        }
    }

    private void runEntityCRUD(EntityManager em) {
        Member member = Member.builder()
                .id("happykoo")
                .userName("해피쿠")
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
        List<Member> members = em.createQuery("select m from MemberV1 m", Member.class).getResultList();
        assertEquals(1, members.size());

        //삭제
        em.remove(member);

        assertEquals(0, em.createQuery("select m from MemberV1 m", Member.class).getResultList().size());
    }

    private void runEntityDetachAndMerge(EntityManager em) {
        Member member = Member.builder()
                .id("happykoo")
                .userName("해피쿠")
                .age(33)
                .build();
        em.persist(member);
        em.flush();

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
