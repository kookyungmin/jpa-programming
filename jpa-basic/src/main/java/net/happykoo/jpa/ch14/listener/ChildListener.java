package net.happykoo.jpa.ch14.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.jpa.ch14.entity.Child;

@Slf4j
public class ChildListener {
    @PrePersist
    public void prePersist(Object obj) {
        Child child = (Child) obj;
        log.info("###### {}", child.getName());
    }

    @PostPersist
    public void postPersist(Object obj) {
        Child child = (Child) obj;
        log.info("###### {} : {}", child.getId(), child.getName());
    }
}
