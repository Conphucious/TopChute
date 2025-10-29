package io.github.conphucious.topchute.util;

import io.github.conphucious.topchute.model.OtpRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    public static void evictOtpCodeCache(Cache cache, String emailAddressKey) {
        // Evict if previously in cache since we're generating new code so must invalidate old
        Optional<OtpRequest> otpRequest = retrieveOtpRequestFromOtpCodeCache(cache, emailAddressKey);
        if (otpRequest.isPresent()) {
            cache.evict(emailAddressKey);
            log.info("Evicting record for found record '{}'!", emailAddressKey);
        }

        // Evict any other cache records that are expired.
        // TODO : Need to evict on expired records. Add TTL.
    }

    public static void evictEveryN(Cache cache, String emailAddressKey, int ttlSeconds) {
        CompletableFuture<Boolean> hasEvictedRecord = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(ttlSeconds);
                cache.evictIfPresent(emailAddressKey);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return false;
        });

        hasEvictedRecord.thenAccept(result ->
                log.info("Cache record expired and evicted for '{}' after '{}' seconds.", emailAddressKey, ttlSeconds));
        hasEvictedRecord.join();
    }

}
