package net.happykoo.jpashop.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Item item;

    private int orderPrice;

    private int count;

    @Builder
    public OrderItem(Item item, int orderPrice, int count) {
        this.order = order;
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
        this.item.removeStock(count);
    }

    public void cancel() {
        this.item.addStock(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
