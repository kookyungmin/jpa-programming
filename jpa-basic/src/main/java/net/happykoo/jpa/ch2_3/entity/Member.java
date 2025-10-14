package net.happykoo.jpa.ch2_3.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "MemberV1")
@Table(name = "V1_MEMBER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "ID", unique = true)
    private String id;

    @Column(name = "NAME")
    private String userName;

    //컬럼명 생략 시 필드 명으로 매핑
    private Integer age;
}
