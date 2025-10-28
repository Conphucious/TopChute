package io.github.conphucious.topchute.util;

import io.github.conphucious.topchute.model.OtpRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;

import java.util.Optional;

@Log4j2
@UtilityClass
public class CacheUtil {
    public static final String OTP_CODE = "otpCode";

    public static Optional<OtpRequest> retrieveOtpRequestFromOtpCodeCache(String emailAddress, Cache cache) {
        Cache.ValueWrapper valueWrapper = cache.get(emailAddress);

        if (valueWrapper == null) {
            log.info("No cache found for '{}' in cache '{}'.", emailAddress, CacheUtil.OTP_CODE);
            return Optional.empty();
        }

        log.info("OtpRequest found in cache for '{}'.", emailAddress);
        OtpRequest otpRequest = ((OtpRequest) valueWrapper.get());
        return Optional.ofNullable(otpRequest);
    }

}
