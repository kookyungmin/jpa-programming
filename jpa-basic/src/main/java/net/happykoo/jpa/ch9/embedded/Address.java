package net.happykoo.jpa.ch9.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class Address {
    private String city;
    private String street;
    @Embedded //임베디드 타입을 포함할 수도 다른 엔티티를 참조할 수도 있다.
    private ZipCode zipCode;

    @Override
    public Address clone() {
        return Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode.clone())
                .build();
    }
}
