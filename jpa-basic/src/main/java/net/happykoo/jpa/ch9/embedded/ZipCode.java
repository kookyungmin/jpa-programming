package net.happykoo.jpa.ch9.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ZipCode {
    private String zip;
    private String plusFour;

    @Override
    public ZipCode clone() {
        return ZipCode.builder()
                .zip(zip)
                .plusFour(plusFour)
                .build();
    }
}
