package net.happykoo.jpa.ch9.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.jpa.ch9.embedded.Address;
import net.happykoo.jpa.ch9.embedded.Period;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "MemberV6")
@Table(name = "V6_MEMBER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private int age;

    @Embedded
    private Period workPeriod;

    @Embedded
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
        @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
        @AttributeOverride(name = "zipCode.zip", column = @Column(name = "COMPANY_ZIP")),
        @AttributeOverride(name = "zipCode.plusFour", column = @Column(name = "COMPANY_PLUS_FOUR")),
    }) //컬럼 재정의
    private Address companyAddress;

    //값 타입 하나이상 저장 -> But, 1 대 N 관계 권장
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOODS",
            joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();
}
