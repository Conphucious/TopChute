package io.github.conphucious.topchute.dto.core;

import io.github.conphucious.topchute.model.BoardType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class GameRequestDto {

    @NotEmpty
    @Size(min = 2, message = "emailAddressList must contain at least 2 emails addresses.")
    private List<String> emailAddressList;

    @NotNull(message = "Board type cannot be empty.")
    private BoardType boardType;

}
