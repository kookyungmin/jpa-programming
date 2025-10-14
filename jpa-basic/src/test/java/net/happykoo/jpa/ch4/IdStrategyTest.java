package net.happykoo.jpa.ch4;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch4.entity.Board;
import net.happykoo.jpa.ch4.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdStrategyTest {
    @Test
    @DisplayName("ID 생성 전략 : IDENTITY")
    public void test1() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Member member = Member.builder()
                        .userName("happykoo")
                        .age(33)
                        .build();
                //IDENTITY 전략은 데이터베이스에 데이터가 생성되어야 식별자가 생성되므로 해당 경우는 트랜잭션 쓰기 지연 동작 X
                em.persist(member);
                //ID 가 생성됨
                assertTrue(member.getId() > 0);
            });
        }
    }

    @Test
    @DisplayName("ID 생성 전략 : SEQUENCE")
    public void test2() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction(em -> {
                Board board = new Board();
                em.persist(board);
                assertTrue(board.getId() >= 3);
            });
        }
    }
}
