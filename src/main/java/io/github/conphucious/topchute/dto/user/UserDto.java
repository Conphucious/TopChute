package io.github.conphucious.topchute.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserDto {
    @NotBlank(message = "Name cannot be empty or whitespace.")
    private String name;

    @Email(message = "A valid email address must be provided.")
    private String emailAddress;

}
