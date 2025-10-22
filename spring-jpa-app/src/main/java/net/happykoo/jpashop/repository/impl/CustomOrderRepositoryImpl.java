package net.happykoo.jpashop.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.happykoo.jpashop.domain.Order;
import net.happykoo.jpashop.domain.OrderSearch;
import net.happykoo.jpashop.domain.QOrder;
import net.happykoo.jpashop.repository.CustomOrderRepository;

import java.util.List;

@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    private final JPAQueryFactory query;

    @Override
    public List<Order> search(OrderSearch orderSearch) {
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
