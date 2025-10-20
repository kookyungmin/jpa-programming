package net.happykoo.jpa.ch10;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch10.dto.SearchParam;
import net.happykoo.jpa.ch10.embedded.Address;
import net.happykoo.jpa.ch10.entity.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class QueryDSLTest {
    private static final PersistenceContextHandler pc = new PersistenceContextHandler("happykoo");
    private static List<Team> teams;
    private static List<Member> members;
    private static List<Product> products;

    @BeforeAll
    public static void setup() {
        createDummyData();
    }

    @Test
    @DisplayName("QueryDSL :: 기본 예제")
    public void test1() {
        pc.runTransaction((em, tx) -> {
            String userName = "User_B";
            JPAQueryFactory query = new JPAQueryFactory(em);
            QMember qMember = new QMember("m");
            List<Member> result = query.selectFrom(qMember)
                    .where(qMember.username.eq(userName))
                    .orderBy(qMember.username.desc())
                    .fetch();

            assertEquals(members.stream()
                    .filter(m -> m.getUsername().equals(userName))
                    .collect(Collectors.toList()).size(), result.size());
        });
    }

    @Test
    @DisplayName("QueryDSL :: count")
    public void test2() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            QMember qMember = new QMember("m");
            List<Member> result = query.selectFrom(qMember)
                    .where(qMember.username.startsWith("User_")
                            .and(qMember.age.gt(10)))
                    .orderBy(qMember.username.desc())
                    .fetch(); //fetch: 전체 결과, fetchOne: 정확히 하나의 결과, fetchFirst: 첫번째 결과

            long count = query.select(qMember.count())
                    .from(qMember)
                    .where(qMember.username.startsWith("User_")
                            .and(qMember.age.gt(10)))
                    .fetchOne();

            assertEquals(result.size(), count);
        });
    }

    @Test
    @DisplayName("QueryDSL :: 페이징과 정렬")
    public void test3() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            QProduct qProduct = new QProduct("p");

            List<Product> result = query.selectFrom(qProduct)
                    .where(qProduct.price.gt(2000))
                    .orderBy(qProduct.price.desc(), qProduct.stockAmount.asc())
                    .offset(10)
                    .limit(20)
                    .fetch();
        });
    }

    @Test
    @DisplayName("QueryDSL :: GroupBy")
    public void test4() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            QMember qMember = new QMember("m");

            List<Tuple> result = query
                    .select(qMember.team.name, qMember.count())
                    .from(qMember)
                    .groupBy(qMember.team.name)
                    .fetch();
            for(Tuple tuple : result) {
                String teamName = tuple.get(qMember.team.name);
                long count = tuple.get(qMember.count());
                log.info("{} : {}", teamName, count);
            }
        });
    }

    @Test
    @DisplayName("QueryDSL :: Join")
    public void test5() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            QMember qMember = new QMember("m");
            QTeam qTeam = new QTeam("t");

            List<Tuple> result = query.select(qMember, qTeam)
                    .from(qMember)
                    .innerJoin(qMember.team, qTeam) //fetch Join 시엔 .fetch() 추가
                    .on(qTeam.name.eq("Team Alpha"))
                    .fetch();

            for (Tuple tuple : result) {
                Member member = tuple.get(qMember);
                Team team = tuple.get(qTeam);

                assertEquals(member.getTeam().getName(), team.getName());
            }
        });
    }

    @Test
    @DisplayName("QueryDSL :: 서브쿼리")
    public void test6() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);
            QMember qMember = new QMember("m");
            QTeam qTeam = new QTeam("t");

            List<Member> result = query.select(qMember)
                    .from(qMember)
                    .where(qMember.team.id.in(select(qTeam.id)
                            .from(qTeam)
                            .where(qTeam.name.eq("Team Alpha"))))
                    .fetch();

        });
    }

    @Test
    @DisplayName("QueryDSL :: 동적 쿼리")
    public void test7() {
        pc.runTransaction((em, tx) -> {
            JPAQueryFactory query = new JPAQueryFactory(em);

            SearchParam searchParam = SearchParam.builder()
                    .name("P")
                    .price(300)
                    .build();

            QProduct qProduct = new QProduct("p");

            BooleanBuilder builder = new BooleanBuilder();

            if (searchParam.getName() != null
                    && !searchParam.getName().equals("")) {
                builder.and(qProduct.name.contains(searchParam.getName()));
            }

            if (searchParam.getPrice() != null) {
                builder.and(qProduct.price.goe(searchParam.getPrice()));
            }

            List<Product> result = query.selectFrom(qProduct)
                    .where(builder)
                    .fetch();
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
