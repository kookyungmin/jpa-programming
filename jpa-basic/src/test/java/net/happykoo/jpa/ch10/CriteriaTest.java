package net.happykoo.jpa.ch10;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch10.embedded.Address;
import net.happykoo.jpa.ch10.entity.Member;
import net.happykoo.jpa.ch10.entity.Order;
import net.happykoo.jpa.ch10.entity.Product;
import net.happykoo.jpa.ch10.entity.Team;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class CriteriaTest {
    private static final PersistenceContextHandler pc = new PersistenceContextHandler("happykoo");
    private static List<Team> teams;
    private static List<Member> members;
    private static List<Product> products;

    @BeforeAll
    public static void setup() {
        createDummyData();
    }

    @Test
    @DisplayName("Criteria :: 기본적인 예제")
    public void test1() {
        pc.runTransaction((em, tx) -> {
            //Criteria 는 코드 기반이므로 컴파일 시점에 오류를 발견할 수 있고, 동적 쿼리를 만드는데 좋긴 하지만, 사용 방법이 너무 복잡하고 어려움 -> QueryDSL 을 많이 사용

            //Criteria 쿼리는 JPQL 을 자바 코드로 작성하도록 도와주는 빌더 클래스 API
            CriteriaBuilder cb = em.getCriteriaBuilder();

            //Criteria 생성 및 반환타입 지정
            CriteriaQuery<Member> cq = cb.createQuery(Member.class);

            //쿼리 루트 : FROM 절
            Root<Member> m = cq.from(Member.class);
            //SELECT 절
            cq.select(m);

            List<Member> findMembers = em.createQuery(cq)
                    .getResultList();

            assertEquals(members.size(), findMembers.size());
        });
    }

    @Test
    @DisplayName("Criteria :: 검색 조건 및 정렬")
    public void test2() {
        pc.runTransaction((em, tx) -> {
            String userName = "user_B";
            //Criteria 쿼리는 JPQL 을 자바 코드로 작성하도록 도와주는 빌더 클래스 API
            CriteriaBuilder cb = em.getCriteriaBuilder();

            //Criteria 생성 및 반환타입 지정
            CriteriaQuery<Member> cq = cb.createQuery(Member.class);

            //쿼리 루트 : FROM 절
            Root<Member> m = cq.from(Member.class);

            //검색 조건 정의
            Predicate usernameEqual = cb.equal(m.get("username"), userName);

            //정렬 조건 정의
            jakarta.persistence.criteria.Order ageDesc = cb.desc(m.get("age"));

            //SELECT 절
            cq.select(m)
              .where(usernameEqual)
              .orderBy(ageDesc);

            //Criteria 에서 반환타입을 지정했으므로, createQuery 에서는 다시 반환티입을 지정하지 않아도 된다.
            Member findMember = em.createQuery(cq)
                    .getSingleResult();

            assertEquals(userName, findMember.getUsername());
        });
    }

    @Test
    @DisplayName("Criteria :: 튜플 사용 예제")
    public void test3() {
        pc.runTransaction((em, tx) -> {
            //Criteria 쿼리는 JPQL 을 자바 코드로 작성하도록 도와주는 빌더 클래스 API
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Tuple> cq = cb.createTupleQuery();

            Root<Member> m = cq.from(Member.class);
            jakarta.persistence.criteria.Order order = cb.asc(m.get("username"));

            cq.multiselect(m.get("username").alias("username"),
                    m.get("age").alias("age")) //튜플에서 사용할 별칭 지정
              .orderBy(order);


            List<Tuple> result = em.createQuery(cq)
                    .getResultList();

            List<Member> expected = members.stream()
                    .sorted(Comparator.comparing(Member::getUsername))
                    .collect(Collectors.toList());

            int idx = 0;
            for (Tuple tuple : result) {
                String userName = tuple.get("username", String.class);
                Integer age = tuple.get("age", Integer.class);
                Member expectedMember = expected.get(idx++);

                assertEquals(expectedMember.getUsername(), userName);
                assertEquals(expectedMember.getAge(), age);
            }
        });
    }

    @Test
    @DisplayName("Criteria :: 조인 사용 예제")
    public void test4() {
        pc.runTransaction((em, tx) -> {
            //Criteria 쿼리는 JPQL 을 자바 코드로 작성하도록 도와주는 빌더 클래스 API
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Tuple> cq = cb.createTupleQuery();

            Root<Member> m = cq.from(Member.class);

            //fetch JOIN 방식
            //m.fetch("team", JoinType.LEFT);

            Join<Member, Team> t = m.join("team", JoinType.INNER);

            //INNER, LEFT OUTER JOIN 방식
            cq.multiselect(m.alias("member"), t.alias("team"))
               .where(cb.equal(t.get("name"), "Team Alpha"));

            List<Tuple> result = em.createQuery(cq)
                    .getResultList();

            for(Tuple tuple : result) {
                assertTrue(tuple.get("member") instanceof Member);
                assertTrue(tuple.get("team") instanceof Team);
            }
        });
    }

    @AfterAll
    public static void destroy() {
        if (pc != null) {
            pc.close();
        }
    }

    private static void createDummyData() {
        pc.runTransaction((em, tx) -> {
            Random random = new Random();

            List<String> cities = List.of("Seoul", "Busan", "Incheon", "Daegu", "Gwangju");
            List<String> streets = List.of("Gangnam-daero", "Teheran-ro", "Myeongdong-gil", "Seomyeon-ro", "Dongseong-ro");

            // --- 팀 생성 ---
            Team teamA = Team.builder().name("Team Alpha").build();
            Team teamB = Team.builder().name("Team Beta").build();
            Team teamC = Team.builder().name("Team Gamma").build();
            em.persist(teamA);
            em.persist(teamB);
            em.persist(teamC);
            teams = List.of(teamA, teamB, teamC);

            // --- 상품 생성 ---
            products = IntStream.rangeClosed(1, 5)
                    .mapToObj(i -> Product.builder()
                            .name("Product-" + i)
                            .price(1000 * (i + random.nextInt(5))) // 1000~9000 랜덤
                            .stockAmount(30 + random.nextInt(200)) // 30~230개
                            .build())
                    .collect(Collectors.toList());
            products.forEach(em::persist);

            // --- 회원 생성 ---
            members = IntStream.rangeClosed(1, 6)
                    .mapToObj(i -> Member.builder()
                            .username("user_" + (char) ('A' + i))
                            .age(18 + random.nextInt(30)) // 18~47
                            .team(teams.get(random.nextInt(teams.size())))
                            .build())
                    .collect(Collectors.toList());
            members.forEach(em::persist);

            // --- 주문 생성 ---
            IntStream.rangeClosed(1, 10).forEach(i -> {
                Member member = members.get(random.nextInt(members.size()));
                Product product = products.get(random.nextInt(products.size()));

                Order order = Order.builder()
                        .orderAmount(1 + random.nextInt(10)) // 1~10개
                        .address(Address.builder()
                                .city(cities.get(random.nextInt(cities.size())))
                                .street(streets.get(random.nextInt(streets.size())))
                                .zipCode(String.format("0%d-%04d", random.nextInt(9), random.nextInt(9999)))
                                .build())
                        .member(member)
                        .product(product)
                        .build();
                em.persist(order);
            });

            em.flush();
        });
    }
}
