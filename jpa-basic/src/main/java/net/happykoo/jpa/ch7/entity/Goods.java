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
@AttributeOverride(name = "id", column = @Column(name = "GOODS_ID")) // 부모로 부터 상속받은 속성 재정의
//조인전략 -> 테이블이 정규화되지만 조회할 때, 조인이 많이 사용됨, 데이터 등록 시 insert 여러 번 실행되어야 함
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "D_TYPE") //부모클래스에 존재하는 구분 컬럼 -> 이 컬럼으로 자식 클래스 구분
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class Goods extends BaseEntity {
    private int price;
}
