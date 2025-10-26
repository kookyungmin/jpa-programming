package net.happykoo.jpa.ch16;

import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch16.entity.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TransactionLockTest {
    private PersistenceContextHandler pc = new PersistenceContextHandler("happykoo");
    @BeforeEach
    public void setup() {
        pc.runTransaction((em, tx) -> {
            Reply reply = Reply.builder()
                    .title("test")
                    .build();

            em.persist(reply);
        });
    }

    @Test
    @DisplayName("낙관적 락 :: 정상적인 경우")
    public void test1() {
        pc.runTransaction((em, tx) -> {
            Reply reply = em.find(Reply.class, 1L);
            log.info("###### 트랜잭션 1 조회");
            assertEquals(reply.getVersion(), 0);

            log.info("###### 트랜잭션 1 수정");
            reply.setTitle("test1");
        });

        pc.runTransaction((em, tx) -> {
            Reply reply = em.find(Reply.class, 1L);
            log.info("###### 트랜잭션 2 조회");
            assertEquals(reply.getVersion(), 1);

            log.info("###### 트랜잭션 2 수정");
            reply.setTitle("test1");
        });
    }

    @Test
    @DisplayName("낙관적 락 :: 버전 달라졌는데 수정하는 경우")
    public void test2() throws Exception {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            Exception ex = assertThrows(RollbackException.class, () -> pc.runTransaction((em, tx) -> {
                Reply reply = em.find(Reply.class, 1L);
                log.info("###### 트랜잭션 1 조회");
                assertEquals(reply.getVersion(), 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //트랜잭션 1 1초 뒤 수정
                log.info("###### 트랜잭션 1 수정");
                reply.setTitle("test1"); //예외 발생
            }));

            //비관적 락 에러
            assertEquals(OptimisticLockException.class, ex.getCause().getClass());
        });

        Thread.sleep(100);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            pc.runTransaction((em, tx) -> {
                Reply reply = em.find(Reply.class, 1L);
                log.info("###### 트랜잭션 2 조회");
                assertEquals(reply.getVersion(), 0);
                //트랜잭션 2 먼저 수정
                log.info("###### 트랜잭션 2 수정");
                reply.setTitle("test2");
            });
        });

        CompletableFuture.allOf(future1, future2).get();
    }

    @Test
    @DisplayName("낙관적 락 :: OPTIMISTIC")
    public void test3() throws Exception {
        //OPTIMISTIC : 조회만 해도 다른 트랜잭션에서 수정했으면 예외 발생
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            Exception ex = assertThrows(RollbackException.class, () -> pc.runTransaction((em, tx) -> {
                Reply reply = em.find(Reply.class, 1L, LockModeType.OPTIMISTIC);
                log.info("###### 트랜잭션 1 조회");
                assertEquals(reply.getVersion(), 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));

            //비관적 락 에러
            assertEquals(OptimisticLockException.class, ex.getCause().getClass());
        });

        Thread.sleep(100);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            pc.runTransaction((em, tx) -> {
                Reply reply = em.find(Reply.class, 1L, LockModeType.OPTIMISTIC);
                log.info("###### 트랜잭션 2 조회");
                assertEquals(reply.getVersion(), 0);
                //트랜잭션 2 먼저 수정
                log.info("###### 트랜잭션 2 수정");
                reply.setTitle("test2");
            });
        });

        CompletableFuture.allOf(future1, future2).get();
    }

    @Test
    @DisplayName("낙관적 락 :: OPTIMISTIC_FORCE_INCREMENT")
    public void test4() {
        //OPTIMISTIC_FORCE_INCREMENT : 조회만 해도 버전 증가
        pc.runTransaction((em, tx) -> {
            Reply reply = em.find(Reply.class, 1L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            log.info("###### 트랜잭션 1 조회");
            assertEquals(reply.getVersion(), 0);
        });

        pc.runTransaction((em, tx) -> {
            Reply reply = em.find(Reply.class, 1L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            log.info("###### 트랜잭션 2 조회");
            assertEquals(reply.getVersion(), 1);
        });
    }
}
