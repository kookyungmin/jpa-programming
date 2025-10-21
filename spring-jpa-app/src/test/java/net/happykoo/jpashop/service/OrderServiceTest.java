package net.happykoo.jpashop.service;

import net.happykoo.jpashop.constant.OrderStatus;
import net.happykoo.jpashop.domain.*;
import net.happykoo.jpashop.exception.NotEnoughStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    private Member member;
    private Item item;

    @BeforeEach
    public void setup() {
        member = Member.builder()
                .name("happykoo")
                .address(Address.builder()
                        .city("서울")
                        .street("관악구")
                        .zipcode("1234")
                        .build())
                .build();
        memberService.join(member);

        item = Book.builder()
                .name("아몬드")
                .author("손원평")
                .price(10000)
                .stockQuantity(100)
                .isbn("ABC")
                .build();
        itemService.saveItem(item);
    }

    @Test
    @DisplayName("상품 주문 테스트")
    public void test1() {
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(),
                item.getId(),
                orderCount);

        Order order = orderService.findOrder(orderId);

        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        //총 주문 가격
        assertEquals(item.getPrice() * orderCount, order.getTotalPrice());
        //재고가 줄어야한다.
        assertEquals(98, item.getStockQuantity());
    }

    @Test
    @DisplayName("재고 초과 테스트")
    public void test2() {
        int orderCount = 101;

        assertThrows(NotEnoughStockException.class, () -> {
            Long orderId = orderService.order(member.getId(),
                    item.getId(),
                    orderCount);
        });
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void test3() {
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(),
                item.getId(),
                orderCount);
        orderService.cancelOrder(orderId);

        Order order = orderService.findOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        //재고가 그대로여야 한다.
        assertEquals(100, item.getStockQuantity());
    }

    @Test
    @DisplayName("상품 검색 테스트")
    public void test4() {
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(),
                item.getId(),
                orderCount);

        List<Order> result1 = orderService.findOrders(OrderSearch.builder()
                .orderStatus(OrderStatus.ORDER)
                .build());

        assertEquals(1, result1.size());

        List<Order> result2 = orderService.findOrders(OrderSearch.builder()
                .orderStatus(OrderStatus.CANCEL)
                .build());

        assertEquals(0, result2.size());

        List<Order> result3 = orderService.findOrders(OrderSearch.builder()
                .memberName("happy")
                .build());

        assertEquals(1, result3.size());
    }
}
