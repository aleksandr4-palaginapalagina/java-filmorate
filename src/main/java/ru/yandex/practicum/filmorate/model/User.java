package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class User extends BaseUnit {

    private String name;

    private Set<Integer> friends = new HashSet<>();

    @NotEmpty
    @Pattern(regexp = "^\\S+$")
    private String login;

    @NotEmpty
    @Email(message = "неверно указан email")
    private String email;

    @Past
    private LocalDate birthday;

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
