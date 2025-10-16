package net.happykoo.jpa.ch8.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Parent {
    @Id @GeneratedValue
    private Long id;
    private String name;

    //(orphanRemoval) 고아 객체 제거 -> 부모 엔티티의 컬렉션에서 자식 엔티티 참조만 제거하면 자식 엔티티가 자동으로 삭제
    @OneToMany(mappedBy = "parent",
            cascade = { CascadeType.PERSIST },
            orphanRemoval = true)
    private List<Child> children;


}
