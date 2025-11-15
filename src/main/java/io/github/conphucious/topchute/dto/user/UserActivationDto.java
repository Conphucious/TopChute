package io.github.conphucious.topchute.dto.user;

import io.github.conphucious.topchute.model.ActivationFailureReason;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserActivationDto {

    private UserDto user;
    private int otpCode;
    private ActivationFailureReason failureReason;

}
