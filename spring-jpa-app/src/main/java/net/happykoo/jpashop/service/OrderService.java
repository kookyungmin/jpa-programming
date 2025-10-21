package net.happykoo.jpashop.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.jpashop.domain.*;
import net.happykoo.jpashop.repository.ItemRepository;
import net.happykoo.jpashop.repository.MemberRepository;
import net.happykoo.jpashop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    //주문
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(count)
                .build();

        Order order = Order.builder()
                .orderItems(List.of(orderItem))
                .delivery(delivery)
                .member(member)
                .build();

        orderRepository.save(order);

        return order.getId();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    public Order findOrder(Long id) {
        return orderRepository.findOne(id);
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
