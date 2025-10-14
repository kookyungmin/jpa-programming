package net.happykoo.jpa.ch4.entity;

import jakarta.persistence.*;
import lombok.*;
import net.happykoo.jpa.ch4.constant.RoleType;

import java.time.LocalDateTime;

@Entity(name = "MemberV2")
@Table(name = "V2_MEMBER", uniqueConstraints = {
    @UniqueConstraint(name = "NAME_AGE_UNIQUE", columnNames = { "NAME", "AGE" })
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 10)
    private String userName;

    //컬럼명 생략 시 필드 명으로 매핑
    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    //필드 타입이 문자면 CLOB, 아니면 BLOB
    @Lob
    private String description;

//    LocalDate, LocalDateTime 사용 권장 -> @Temporal 로 매핑할 필요 없음
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createDate;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

}
