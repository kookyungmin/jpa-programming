package net.happykoo.jpa.ch14;

import jakarta.persistence.EntityGraph;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch14.entity.Child;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class EntityGraphTest {
    @Test
    @DisplayName("엔티티 그래프 테스트")
    public void test1() {
//        Child.withParent 쿼리 실행시 parent도 같이 fetch join (fetch join은 JPQL을 사용해야하지만, entity graph는 JPQL 사용 X)
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Child child = Child.builder()
                        .name("test")
                        .build();
                EntityGraph graph = em.getEntityGraph("Child.withParent");

                em.persist(child);
                em.clear();

                Map<String, Object> hints = new HashMap<>();
                hints.put("jakarta.persistence.fetchgraph", graph);

                //Lazy Loading 이지만 Child.withParent 쿼리 실행시 join 되어 조회
                Child findChild = em.find(Child.class, child.getId(), hints);
            });

        }
    }
}
