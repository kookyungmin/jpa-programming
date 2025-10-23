package net.happykoo.jpa.ch14;

import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch14.entity.Child;
import net.happykoo.jpa.ch14.entity.Parent;
import net.happykoo.jpa.ch14.entity.Teacher;
import org.hibernate.collection.spi.PersistentBag;
import org.hibernate.collection.spi.PersistentList;
import org.hibernate.collection.spi.PersistentSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CollectionTest {
    @Test
    @DisplayName("List, Collection 은 PersistentBag")
    public void test1() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                //PersistentBag -> 중복 허용, 순서 X
                Child child = Child.builder()
                        .name("marco")
                        .build();

                em.persist(child);

                assertEquals(PersistentBag.class, child.getOrders().getClass());
            });
        }
    }

    @Test
    @DisplayName("Set 은 PersistentSet")
    public void test2() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                //PersistentSet -> 중복 X, 순서 X
                Parent parent = Parent.builder()
                        .name("happykoo")
                        .build();
                em.persist(parent);

                assertEquals(PersistentSet.class, parent.getChildren().getClass());
            });
        }
    }

    @Test
    @DisplayName("@OrderColumn + List는 PersistentList")
    public void test3() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                //PersistentSet -> 중복 O, 순서 O -> 잘 사용하지 않음 개발자가 직접 order seq 관리하는게 좋음
                Teacher teacher = Teacher.builder()
                        .name("happykoo")
                        .build();
                em.persist(teacher);

                assertEquals(PersistentList.class, teacher.getStudents().getClass());
            });
        }
    }
}
