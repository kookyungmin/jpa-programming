package net.happykoo.jpa.ch16.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
public class Reply {
    @Id @GeneratedValue
    private Long id;

    @Setter
    private String title;

    //낙관적 락 사용 -> 엔티티를 수정할 때마다 version이 증가 (조회 시점의 버전과 수정 시점의 버전이 다르면 예외 발생)
    @Version
    private Integer version;

    @Builder
    public Reply(String title) {
        this.title = title;
    }
}
