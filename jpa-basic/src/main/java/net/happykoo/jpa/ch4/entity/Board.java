package net.happykoo.jpa.ch4.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "BOARD")
@Getter
public class Board {
    @Id
    //strategy = GenerationType.TABLE 쓰면 테이블을 이용해 시퀀스를 흉내냄(모든 데이터베이스 사용가능)
    //strategy = GenerationType.AUTO 쓰면 DBMS 별로 알아서 IDENTITY, SEQUENCE 적용해줌
    @SequenceGenerator(name = "BOARD_SEQ_GENERATOR",
            //실제 데이터베이스 시퀀스 명
            sequenceName = "BOARD_SEQ",
            initialValue = 3,
            //기본적으로 50 이 default, 1 ~ 50 단위로 미리 메모리에 캐시해놓기 때문
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    private Long id;
}
