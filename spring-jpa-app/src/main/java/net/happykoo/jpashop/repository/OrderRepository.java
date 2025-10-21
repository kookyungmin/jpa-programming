package net.happykoo.jpashop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.happykoo.jpashop.domain.Order;
import net.happykoo.jpashop.domain.OrderSearch;
import net.happykoo.jpashop.domain.QOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepository {
    @PersistenceContext
    private EntityManager em;

    private JPAQueryFactory query;


    @PostConstruct
    public void init() {
        query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder qOrder = new QOrder("o");
        BooleanBuilder builder = new BooleanBuilder();

        if (orderSearch.getOrderStatus() != null) {
            builder.and(qOrder.status.eq(orderSearch.getOrderStatus()));
        }

        if (orderSearch.getMemberName() != null &&
            !orderSearch.getMemberName().equals("")) {
            builder.and(qOrder.member.name.contains(orderSearch.getMemberName()));
        }

        return query.selectFrom(qOrder)
                .where(builder)
                .offset(0)
                .limit(1000)
                .fetch();
    }
}
