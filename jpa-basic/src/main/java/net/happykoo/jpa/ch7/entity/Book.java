package net.happykoo.jpa.ch7.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B") //구분 값
@PrimaryKeyJoinColumn(name = "BOOK_ID") //부모의 primary key 컬럼명 변경 시 사용
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Book extends Goods {
    private String author;
    private String isbn;
}
