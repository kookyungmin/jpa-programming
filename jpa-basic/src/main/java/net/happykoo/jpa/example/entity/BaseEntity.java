package net.happykoo.jpa.example.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
}
