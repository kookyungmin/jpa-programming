package net.happykoo.jpa.ch8;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch8.entity.Child;
import net.happykoo.jpa.ch8.entity.Parent;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CascadeTest {
    @Test
    @DisplayName("영속성 전이 테스트")
    public void test1() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Parent parent = Parent.builder()
                        .name("parent")
                        .build();
                Child child = Child.builder()
                        .name("child")
                        .parent(parent)
                        .build();
                em.persist(child);
                //persist, remove 는 flush 후 전이됨
                //해당 경우는 예제일 뿐이며, 보통 OneToMany 에서 사용하여 부모가 제거될 때 자식까지 모두 제거시킬 때 사용
                em.flush();

                //연관관계까지도 영속성 추가됨
                assertTrue(Hibernate.isInitialized(parent));
            });
        }
    }

    @Test
    @DisplayName("orphanRemoval :: 고아 객체 제거")
    public void test2() {
        try (PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Parent parent = Parent.builder()
                        .name("parent")
                        .children(new ArrayList<>())
                        .build();
                Child child = Child.builder()
                        .name("child")
                        .parent(parent)
                        .build();

                parent.getChildren().add(child);
                em.persist(parent);
                em.flush();

                assertTrue(Hibernate.isInitialized(child));

                //부모에게서 자식을 제거하면, 자식은 고아객체가 되어 제거됨
                parent.getChildren().remove(child);
                em.flush();

                //주의: orphanRemoval 는 delete query 만 날릴뿐, 영속성에서는 그대로 남아있음(But, 삭제 된 상태임)
                assertTrue(Hibernate.isInitialized(child));
                Child findChild = em.find(Child.class, child.getId());
                assertNull(findChild);
            });
        }
    }
}
