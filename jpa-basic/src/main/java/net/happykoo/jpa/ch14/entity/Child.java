package net.happykoo.jpa.ch14.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.jpa.ch14.converter.BooleanToYNConverter;
import net.happykoo.jpa.ch14.listener.ChildListener;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(ChildListener.class)
//Child.withParent 쿼리 실행시 parent도 같이 fetch join (fetch join은 JPQL을 사용해야하지만, entity graph는 JPQL 사용 X)
@NamedEntityGraph(name = "Child.withParent", attributeNodes = {
    @NamedAttributeNode("parent")
})
public class Child {
    @Id
    @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @OneToMany(mappedBy = "child")
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    //boolean -> Y, N 으로 변환해서 데이터베이스에 저장
    @Convert(converter = BooleanToYNConverter.class)
    private boolean vip;
}
