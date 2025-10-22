package net.happykoo.jpashop.domain;

import lombok.*;
import net.happykoo.jpashop.constant.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import static net.happykoo.jpashop.repository.spec.OrderSpec.memberNameLike;
import static net.happykoo.jpashop.repository.spec.OrderSpec.orderStatusEq;
import static org.springframework.data.jpa.domain.Specification.where;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchSpecification {
    private String memberName;
    private OrderStatus orderStatus;

    public Specification<Order> toSpecification() {
        return where(memberNameLike(memberName))
                .and(orderStatusEq(orderStatus));
    }
}
