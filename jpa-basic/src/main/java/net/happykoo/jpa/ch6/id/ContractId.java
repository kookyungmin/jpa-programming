package net.happykoo.jpa.ch6.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ContractId implements Serializable {
    private Long member;
    private Long company;
}
