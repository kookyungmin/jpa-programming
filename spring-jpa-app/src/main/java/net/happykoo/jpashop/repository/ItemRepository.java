package net.happykoo.jpashop.repository;

import net.happykoo.jpashop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
