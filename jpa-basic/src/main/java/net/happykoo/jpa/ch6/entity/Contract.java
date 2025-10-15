package net.happykoo.jpa.ch6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.happykoo.jpa.ch6.id.ContractId;

import java.time.LocalDateTime;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
//복합키 매핑 -> 보통 복합키를 사용하면 복잡하므로 매핑엔티티에 기본키를 두고 외래키로 참조한다.
@IdClass(ContractId.class)
public class Contract {
    @Id
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    private LocalDateTime contractDate;
}
