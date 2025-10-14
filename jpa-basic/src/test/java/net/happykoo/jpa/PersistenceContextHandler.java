package net.happykoo.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class PersistenceContextHandler implements AutoCloseable {
    private final EntityManagerFactory emf;
    private final String unitName;

    public PersistenceContextHandler(String unitName) {
        this.unitName = unitName;
        this.emf = Persistence.createEntityManagerFactory(unitName);
    }

    public void runTransaction(Consumer<EntityManager> fn) {
        //Persistence Context 장점
        //1. 1차 캐시 -> Entity 들을 @Id 기반으로 1차 캐시로 관리하여 find 로 데이터를 찾을 때, 1차캐시에서 먼저 찾은 후 없으면 데이터베이스에서 조회한다.
        //2. 트랜잭션 쓰기 지연 -> commit 전까지의 SQL 를 모아놨다가 커밋할 때 모아둔 쿼리를 데이터베이스에 보냄(flush: 데이터베이스와 동기화)
        //3. 변경 감지 -> 엔티티의 변경사항을 데이터베이스에 자동으로 반영 -> 엔티티의 최초상태를 스냅샷으로 저장 -> flush 시점에 스냅샷과 엔티티를 비교해서 변경된 엔티티 찾음
        //위의 모든 작업은 EntityManager 의 생명주기 동안에만 유지된다.

        EntityTransaction tx = null;
        //엔티티 매니저 팩토리 생성 (원래 엔티티 매니저 팩토리는 생성 비용이 크기에 싱글턴으로 관리)
        //엔티티 매니저 생성 -> 엔티티를 데이터베이스에 등록/수정/삭제/조회할 수 있음 -> 스레드 간 공유해서는 안됨
        try(EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();

            tx.begin();
            //메인 로직(모두 1차 캐시되고, 트랜잭션 쓰기 지연이 되어 commit 전까지는 Persistence Context 에 저장된다)
            fn.accept(em);
            //commit 후에 비로소 데이터베이스로 SQL 실행
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        if (emf != null
                && emf.isOpen()) {
            emf.close();
        }
    }
}
