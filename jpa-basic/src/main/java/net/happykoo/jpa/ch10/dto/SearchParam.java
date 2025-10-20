package net.happykoo.jpa.ch10.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchParam {
    private String name;
    private Integer price;
}
