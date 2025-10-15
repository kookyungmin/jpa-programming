package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "MemberV4")
@Table(name = "V4_MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    //1:1 관계, Member(주 테이블)에 FK 생성
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    //N: N 관계, 조인테이블 생성
    //N:N 관계도 거의 안씀 -> 직접 매핑테이블 만들어서 관리
    //매핑 테이블에 다른 컬럼들도 넣을 수 있기에.. -> 1:N N:1 관계로 대체
    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT",
        joinColumns = @JoinColumn(name = "MEMBER_ID"),
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
    private List<Product> products;

    //N:N 관계를 매핑테이블 엔티티로 변경
    @OneToMany(mappedBy = "member")
    private List<Contract> contracts;
}
