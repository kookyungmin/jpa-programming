package net.happykoo.jpa.ch9.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
