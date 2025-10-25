package net.happykoo.jpashop.exception;

import net.happykoo.jpashop.domain.Member;
import net.happykoo.jpashop.domain.Order;
import net.happykoo.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SpringExceptionTranslationTest {
    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("스프링 예외 전환 테스트 :: 변환 안된 경우")
    public void test1() {
        //EntityManager 를 직접 사용한 경우는 스프링 변환 X
        assertThrows(javax.persistence.NoResultException.class, () -> {
            EntityManager em = emf.createEntityManager();
            em.createQuery("select m from Member m", Member.class)
                    .getSingleResult();
        });
    }
    //원래 javax.persistence.NoResultException 발생하지만, 스프링이 org.springframework.dao.EmptyResultDataAccessException으로 변환
    @Test
    @DisplayName("스프링 예외 전환 테스트 :: 변환 된 경우")
    public void test2() {
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> {
            Order order = orderRepository.findSingleOrder();
        });
    }
}
