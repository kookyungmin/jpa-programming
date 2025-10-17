package net.happykoo.jpa.ch10;

import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch10.dto.UserDTO;
import net.happykoo.jpa.ch10.embedded.Address;
import net.happykoo.jpa.ch10.entity.Member;
import net.happykoo.jpa.ch10.entity.Order;
import net.happykoo.jpa.ch10.entity.Product;
import net.happykoo.jpa.ch10.entity.Team;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class JPQLTest {
    private static final PersistenceContextHandler pc = new PersistenceContextHandler("happykoo");
    private static List<Team> teams;
    private static List<Member> members;
    private static List<Product> products;

    @BeforeAll
    public static void setup() {
        createDummyData();
    }

    @Test
    @DisplayName("JPQL :: TypedQuery, Query 예제")
    public void test1() {
        pc.runTransaction((em, tx) -> {
            //반환할 타입을 명확하게 지정할 수 있으면 TypedQuery, 그렇지 않으면 Query
            TypedQuery<Member> query = em.createQuery("SELECT m FROM MemberV7 m", Member.class);
            List<Member> resultList = query.getResultList();
            assertEquals(members.size(), resultList.size());

            //조회 대상 타입이 명확하지 않으면 Query 사용 -> Query를 사용하면 결과가 Object로 반환
            List<Object[]> q = em.createQuery("SELECT m.username, m.age FROM MemberV7 m").getResultList();
            //컬럼이 둘 이상이면 Object[] 반환
            Object[] o = q.get(0);
            assertTrue(o[0] instanceof String);
            assertTrue(o[1] instanceof Integer);
        });
    }

    @Test
    @DisplayName("JPQL :: 파라미터 바인딩 예제")
    public void test2() {
        pc.runTransaction((em, tx) -> {
            //파라미터 바인딩을 사용하면 SQL로 파싱한 결과를 재사용할 수 있어서 전체적인 성능 향상에 도움이 된다.

            String userName = "user_B";
            //이름 기준 파라미터
            Member findMember1 = em.createQuery("SELECT m FROM MemberV7 m WHERE m.username = :username", Member.class)
                    .setParameter("username", userName)
                    .getSingleResult();
            assertEquals(userName, findMember1.getUsername());

            //위치 기준 파라미터 -> 이름 기준을 권장
            Member findMember2 = em.createQuery("SELECT m FROM MemberV7 m WHERE m.username = ?1", Member.class)
                    .setParameter(1, userName)
                    .getSingleResult();
            assertEquals(userName, findMember2.getUsername());
        });
    }

    @Test
    @DisplayName("JPQL :: 프로젝션 예제")
    public void test3() {
        pc.runTransaction((em, tx) -> {
            //SELECT 절에 조회할 대상을 지정하는 것을 프로젝션이라고 함
            String userName = "user_B";
            Member expectedMember = members.stream()
                    .filter(m -> m.getUsername().equals(userName))
                    .findFirst()
                    .get();

            //1. 엔티티 프로젝션
            Team team = em.createQuery("SELECT m.team FROM MemberV7 m WHERE m.username = :username", Team.class)
                    .setParameter("username", userName)
                    .getSingleResult();

            assertEquals(expectedMember.getTeam().getName(), team.getName());

            //2. 임베디드 타입 프로젝션
            List<Address> addresses = em.createQuery("SELECT o.address FROM OrderV2 o", Address.class)
                    .getResultList();
            assertTrue(addresses.size() > 0);

            //3. 스칼라 타입 프로젝션
            List<String> names = em.createQuery("SELECT DISTINCT m.username FROM MemberV7 m", String.class)
                    .getResultList();
            assertTrue(names.size() > 0);


            //여러 값 조회 시엔 TypedQuery를 사용할 수 없고, Query 를 사용해야함 -> Object 배열로 관리
            List<Object[]> q = em.createQuery("SELECT m.username, m.age FROM MemberV7 m").getResultList();
            //컬럼이 둘 이상이면 Object[] 반환
            Object[] o = q.get(0);
            assertTrue(o[0] instanceof String);
            assertTrue(o[1] instanceof Integer);

            //여러 값 조회 시 DTO 를 생성한 후 new 명령어를 사용할 수도 있다.
            UserDTO user = em.createQuery("SELECT new net.happykoo.jpa.ch10.dto.UserDTO(m.username, m.age, m.team) " +
                            "FROM MemberV7 m WHERE username = :username", UserDTO.class)
                    .setParameter("username", userName)
                    .getSingleResult();
            assertEquals(expectedMember.getUsername(), user.getUsername());
            assertEquals(expectedMember.getTeam().getName(), user.getTeam().getName());
        });
    }

    @Test
    @DisplayName("JPQL :: 페이징 예제")
    public void test4() {
        pc.runTransaction((em, tx) -> {
            //페이징 쿼리를 DBMS 에 맞게 일일히 작성할 필요 없음
            //JPA 는 다음 두 API 로 페이징을 추상화 함
            //setFirstResult(int startPosition) : 조회 시작 위치(0부터 시작)
            //setMaxResults(int maxResult) : 조회 할 데이터 수

            List<Member> result = em.createQuery("SELECT m FROM MemberV7 m ORDER BY m.username", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            assertEquals(2, result.size());
        });
    }

    @Test
    @DisplayName("JPQL :: 집합과 정렬 예제")
    public void test5() {
        pc.runTransaction((em, tx) -> {
            
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
