package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.util.CacheUtil;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class OtpService { // TODO : separate into email service
    // TODO : make this an hour.
    private static final int ttlSeconds = 60 * 5 * 3; // 15 mins
    private static final SecureRandom secureRandom = new SecureRandom();

    private final EmailService emailService;
    private final CacheManager cacheManager;
    // TODO : register shows different email


    @Autowired
    public OtpService(EmailService emailService, CacheManager cacheManager) {
        this.emailService = emailService;
        this.cacheManager = cacheManager;
    }

    public boolean registerUser(String emailAddress) {
        return false;
    }

    //    @CachePut(cacheNames = "otpReqs", key = "emailAddress")
    // TODO : make it so it says "emailAddress sent you an invite to play Top Chute. Click the link here or go to http://asdasd.com/code and enter this OTP: code. This invite expires in 1 hr"
    public OtpRequest sendGameInvite(String emailAddress) {
        OtpRequest otpRequest = generateOtpRequest(emailAddress);
        try {
            emailService.sendGameInvite(otpRequest);
            otpRequest.setEmailSentSuccessfully(true);
        } catch (MessagingException e) {
            log.info("Failed to send game invite OTP email to '{}' with msg '{}'", otpRequest.getEmailAddress(), e);
            otpRequest.setEmailSentSuccessfully(false);
        }
        return otpRequest;
    }

    @CachePut(cacheNames = "otpCode", value = "otpCode", key = "#emailAddress")
    public OtpRequest sendRegisterInvite(String emailAddress) {
        log.info("Sending registration OTP email to '{}'", emailAddress);

        OtpRequest otpRequest = generateOtpRequest(emailAddress);
        try {
            // TODO : Finish this URL: ${HOST}.com/otp/${otp} and have that activate
            emailService.sendRegisterInvite(otpRequest);
            otpRequest.setEmailSentSuccessfully(true);
        } catch (MessagingException e) {
            log.info("Failed to send register OTP email to '{}' with msg '{}'", otpRequest.getEmailAddress(), e);
            otpRequest.setEmailSentSuccessfully(false);
        }
        return otpRequest;
    }

    @Cacheable(value = CacheUtil.OTP_CODE, key = "#emailAddress")
    private OtpRequest generateOtpRequest(String emailAddress) {
        Cache cache = cacheManager.getCache(CacheUtil.OTP_CODE);
        if (Optional.ofNullable(cache).isPresent()) {
            CacheUtil.evictOtpCodeCache(cache, emailAddress);
        }

        log.info("Generating OTP request for '{}'", emailAddress);
        int otp = generateOtp();
        OtpRequest otpRequest = new OtpRequest(emailAddress, otp, false, Instant.now().plusSeconds(ttlSeconds));

        // TODO : I am creating an async task to evict. Need to test
        CacheUtil.evictEveryN(cache, emailAddress, ttlSeconds);
        return otpRequest;
    }

    private int generateOtp() {
        return secureRandom.nextInt((int) Math.pow(10, 6));
    }

}
