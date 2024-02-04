package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Film extends BaseUnit {

    @NotBlank(message = "название не может быть пустым")
    @NotNull
    private String name;

    @Size(max = 200, message = "длина превышает 200 символов")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    private Set<Integer> userId = new HashSet<>();

    private int rate = 0;

    public void addLike(int id) {
        userId.add(id);
        rate = userId.size();
    }

    public void removeLike(int id) {
        userId.remove(id);
        rate = userId.size();
    }

}
