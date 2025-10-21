package net.happykoo.jpashop.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.jpashop.domain.Item;
import net.happykoo.jpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    public void saveItem(Item item) {
        repository.save(item);
    }

    public List<Item> findItems() {
        return repository.findAll();
    }

    public Item findOne(Long itemId) {
        return repository.findOne(itemId);
    }
}
