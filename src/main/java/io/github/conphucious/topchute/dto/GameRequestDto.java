package io.github.conphucious.topchute.dto;

import io.github.conphucious.topchute.model.map.BoardType;
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
    @Size(min = 2, message = "PN list must contain at least 2 phone numbers.")
    private List<String> emailAddressList;

    @NotNull(message = "Board type cannot be empty.")
    private BoardType boardType;

}
