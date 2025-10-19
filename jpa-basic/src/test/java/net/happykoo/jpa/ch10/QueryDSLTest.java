package net.happykoo.jpa.ch10;

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

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QueryDSLTest {
    private static final PersistenceContextHandler pc = new PersistenceContextHandler("happykoo");
    private static List<Team> teams;
    private static List<Member> members;
    private static List<Product> products;

    @BeforeAll
    public static void setup() {
        createDummyData();
    }

    @Test()
    @DisplayName("")
    public void test1() {

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
