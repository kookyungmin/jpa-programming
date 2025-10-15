package net.happykoo.jpa.ch6;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch6.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EntityMappingTest {
    @Test
    @DisplayName("1 : N 관계 단일관계인 경우")
    public void test1() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Member m1 = Member.builder()
                        .name("happykoo")
                        .build();
                Member m2 = Member.builder()
                        .name("marco")
                        .build();
                Team team = Team.builder()
                        .name("happyteam")
                        .memberList(new ArrayList<>())
                        .build();

                team.getMemberList().add(m1);
                team.getMemberList().add(m2);

                em.persist(m1);
                em.persist(m2);

                //FK가 Member 테이블에 존재하므로, member 수만큼 update 문 실행됨
                //1:N 관계의 경우 엔티티를 매핑한 테이블이 아닌 다른 테이블의 외래 키를 관리해야 함 -> 성능 및 관리 부담
                //N:1 관계를 사용하자!
                em.persist(team);
            });
        }
    }

    @Test
    @DisplayName("1 : 1 관계 양방향 관계인 경우")
    public void test2() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Member member = Member.builder()
                        .name("happykoo")
                        .build();
                Locker locker = Locker.builder()
                        .name("happylocker")
                        .build();
                em.persist(member);
                em.persist(locker);

                member.setLocker(locker);
                //연관관계 주인이 아니므로 update 쿼리 X
                locker.setMember(member);
            });
        }
    }

    @Test
    @DisplayName("N:N 관계")
    public void test3() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Product p1 = Product.builder()
                        .name("product 1")
                        .build();
                Product p2 = Product.builder()
                        .name("Product 2")
                        .build();
                Member m1 = Member.builder()
                        .name("marco")
                        .products(new ArrayList<>())
                        .build();
                Member m2 = Member.builder()
                        .name("happykoo")
                        .products(new ArrayList<>())
                        .build();

                em.persist(p1);
                em.persist(p2);
                em.persist(m1);
                em.persist(m2);

                //아래 연산 들도 Persistence Context에서 변경감지 -> SQL 실행됨
                m1.getProducts().add(p1);
                m1.getProducts().add(p2);
                m2.getProducts().add(p1);
                m2.getProducts().add(p2);
                m1.getProducts().remove(p1);
            });
        }
    }

    @Test
    @DisplayName("N:N -> 매핑클래스 복합키 테스트")
    public void test4() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Company c1 = Company.builder()
                        .name("company 1")
                        .build();
                Company c2 = Company.builder()
                        .name("company 2")
                        .build();
                Member m1 = Member.builder()
                        .name("marco")
                        .products(new ArrayList<>())
                        .build();
                Member m2 = Member.builder()
                        .name("happykoo")
                        .products(new ArrayList<>())
                        .build();

                em.persist(c1);
                em.persist(c2);
                em.persist(m1);
                em.persist(m2);

                Contract contract1 = Contract.builder()
                        .member(m1)
                        .company(c1)
                        .contractDate(LocalDateTime.now())
                        .build();

                Contract contract2 = Contract.builder()
                        .member(m1)
                        .company(c2)
                        .contractDate(LocalDateTime.now())
                        .build();

                Contract contract3 = Contract.builder()
                        .member(m2)
                        .company(c1)
                        .contractDate(LocalDateTime.now())
                        .build();

                Contract contract4 = Contract.builder()
                        .member(m2)
                        .company(c2)
                        .contractDate(LocalDateTime.now())
                        .build();

                em.persist(contract1);
                em.persist(contract2);
                em.persist(contract3);
                em.persist(contract4);
            });
        }
    }
}
