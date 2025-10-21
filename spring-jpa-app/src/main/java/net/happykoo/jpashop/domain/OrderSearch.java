package net.happykoo.jpashop.domain;

import lombok.*;
import net.happykoo.jpashop.constant.OrderStatus;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
