package net.happykoo.jpa.ch7.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//JPA 상속 예제
//1. 조인 전략: 부모, 자식 엔티티 모두 테이블로 만들고, 조회할 때 조인한다.
//2. 단일 테이블 전략: 부모, 자식 엔티티를 통합하여 하나의 테이블로 만든다.
//3. 구현 클래스마다 테이블 전략: 자식 엔티티에 부모 필드를 모두 포함하여 각각 테이블로 만든다. -> 정규형도 지키지 않고 여러 자식 테이블을 통합해서 조회하기도 어렵고, 성능도 나쁨 (사용 X)
@Entity
@AttributeOverride(name = "id", column = @Column(name = "FOOD_ID")) // 부모로 부터 상속받은 속성 재정의
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//단일 테이블 전략 -> 조인이 필요 없으므로 조회 성능이 빠르지만, 자식 엔티티가 매핑한 컬럼은 모두 null 허용해야 함(다른 자식에서는 사용 안하므로), 테이블이 커질 수 있다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "F_TYPE")
@Getter
public abstract class Food extends BaseEntity {
    private int calories;
}
