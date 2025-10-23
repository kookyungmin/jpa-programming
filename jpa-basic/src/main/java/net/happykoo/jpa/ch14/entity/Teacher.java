package net.happykoo.jpa.ch14.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Teacher {
    @Id
    @GeneratedValue
    @Column(name = "TEACHER_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "teacher")
    @OrderColumn(name = "name")
    @Builder.Default
    private List<Child> students  = new ArrayList<>();
}
