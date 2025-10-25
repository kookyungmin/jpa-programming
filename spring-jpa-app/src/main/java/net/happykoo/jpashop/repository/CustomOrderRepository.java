package net.happykoo.jpashop.repository;

import net.happykoo.jpashop.domain.Order;
import net.happykoo.jpashop.domain.OrderSearch;

import java.util.List;

public interface CustomOrderRepository {
    List<Order> search(OrderSearch orderSearch);
    Order findSingleOrder();
}
