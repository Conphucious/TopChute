package io.github.conphucious.topchute.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class OtpRequest {
    private String emailAddress;
    private int otp;
    private boolean isEmailSentSuccessfully;
    private Instant expiresAt;

    public enum Type {
        OTP,
        REGISTER_OTP
    }

}
