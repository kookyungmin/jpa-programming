package net.happykoo.jpa.ch7;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch7.constant.ChickenGender;
import net.happykoo.jpa.ch7.entity.Book;
import net.happykoo.jpa.ch7.entity.Chicken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InheritMappingTest {
    @Test
    @DisplayName("상속 매핑 :: 조인 전략 테스트")
    public void test1() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Book book = Book.builder()
                        .author("손원평")
                        .name("아몬드")
                        .price(10000)
                        .build();

                em.persist(book);
                tx.commit();
                em.clear();

                Book findBook = em.find(Book.class, book.getId());
                assertEquals(book.getName(), findBook.getName());
                assertEquals(book.getAuthor(), findBook.getAuthor());
            });
        }
    }

    @Test
    @DisplayName("상속 매핑 :: 단일 테이블 전략 테스트")
    public void test2() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Chicken chicken = Chicken.builder()
                        .name("치키치키")
                        .gender(ChickenGender.MALE)
                        .calories(300)
                        .build();

                em.persist(chicken);
                tx.commit();
                em.clear();

                Chicken findChicken = em.find(Chicken.class, chicken.getId());
                assertEquals(chicken.getName(), findChicken.getName());
                assertEquals(chicken.getGender(), findChicken.getGender());
            });
        }
    }
}
