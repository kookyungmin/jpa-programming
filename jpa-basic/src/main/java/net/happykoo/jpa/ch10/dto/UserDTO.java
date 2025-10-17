package net.happykoo.jpa.ch10.dto;

import lombok.*;
import net.happykoo.jpa.ch10.entity.Team;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private int age;
    private Team team;
}
