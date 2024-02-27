package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class User extends BaseUnit {

    private String name;


    @NotEmpty
    @Pattern(regexp = "^\\S+$")
    private String login;

    @NotEmpty
    @Email(message = "неверно указан email")
    private String email;

    @Past
    private LocalDate birthday;


}
