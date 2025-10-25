package io.github.conphucious.topchute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserDto {
    @NotBlank(message = "Name cannot be empty or whitespace.")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits long.")
    private String phoneNumber;

}
