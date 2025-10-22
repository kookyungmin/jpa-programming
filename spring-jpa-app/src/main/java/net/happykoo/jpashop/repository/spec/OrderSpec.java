package net.happykoo.jpashop.repository.spec;

import net.happykoo.jpashop.constant.OrderStatus;
import net.happykoo.jpashop.domain.Member;
import net.happykoo.jpashop.domain.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class OrderSpec {
    public static Specification<Order> memberNameLike(final String memberName) {
        return ((root, query, criteriaBuilder) -> {
            if (memberName == null || memberName.equals("")) {
                return null;
            }

            Join<Order, Member> m = root.join("member", JoinType.INNER);
            return criteriaBuilder.like(m.get("name"), "%" + memberName + "%");
        });
    }

    public static Specification<Order> orderStatusEq(final OrderStatus orderStatus) {
        return ((root, query, criteriaBuilder) -> {
            if (orderStatus == null) return null;

            return criteriaBuilder.equal(root.get("status"), orderStatus);
        });
    }
}
