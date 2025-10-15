package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Locker {
    @Id
    @Column(name = "LOCKER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    //연관관계 주인 명시
    @OneToOne(mappedBy = "locker")
    private Member member;
}
