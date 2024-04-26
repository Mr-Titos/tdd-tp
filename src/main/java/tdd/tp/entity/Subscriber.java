package tdd.tp.entity;

import lombok.*;
import tdd.tp.enums.GenderEnum;

import java.time.LocalDate;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Subscriber {

    private String code;

    private String name;

    private String firstName;

    private String mail;

    private LocalDate birthdate;

    private GenderEnum gender;
}
