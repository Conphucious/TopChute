package io.github.conphucious.topchute.dto.core;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GameActionDto {

    @Email(message = "A valid email address must be provided.")
    private String emailAddress;

    @NotNull(message = "Action type cannot be empty.")
    private GameActionType actionType;

}

