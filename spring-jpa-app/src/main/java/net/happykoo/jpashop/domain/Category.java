package net.happykoo.jpashop.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @ManyToMany(mappedBy = "category")
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    //연관관계 메서드
    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }
}
