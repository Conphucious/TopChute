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

    public static Optional<OtpRequest> retrieveOtpRequestFromOtpCodeCache(Cache cache, String emailAddress) {
        if (cache == null) {
            log.warn("Cache '{}' is null", CacheUtil.OTP_CODE);
            return Optional.empty();
        }

        Cache.ValueWrapper valueWrapper = cache.get(emailAddress);
        if (valueWrapper == null) {
            log.info("No cache found for '{}' in cache '{}'.", emailAddress, CacheUtil.OTP_CODE);
            return Optional.empty();
        }

        log.info("OtpRequest found in cache for '{}'.", emailAddress);
        OtpRequest otpRequest = ((OtpRequest) valueWrapper.get());
        return Optional.ofNullable(otpRequest);
    }

    public static void evictOtpCodeCache(Cache cache, String key) {
        Optional<OtpRequest> otpRequest = retrieveOtpRequestFromOtpCodeCache(cache, key);
        if (otpRequest.isPresent()) {
            cache.evict(key);
            log.info("Evicting record for found record '{}'!", key);
        }
    }

}
