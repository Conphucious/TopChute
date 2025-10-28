package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.util.HtmlUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Log4j2
@Service
public class OtpService {

    // TODO : make this an hour.
    private static final int ttlSeconds = 60 * 5; // 5 mins
    private static final SecureRandom secureRandom = new SecureRandom();

    private final JavaMailSender javaMailSender;
    // TODO : register shows different email


    @Autowired
    public OtpService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean registerUser(String emailAddress) {
        return false;
    }

    //    @CachePut(cacheNames = "otpReqs", key = "emailAddress")
    // TODO : make it so it says "emailAddress sent you an invite to play Top Chute. Click the link here or go to http://asdasd.com/code and enter this OTP: code. This invite expires in 1 hr"
    public OtpRequest sendGameInviteOtp(String emailAddress) {
        OtpRequest otpRequest = generateOtpRequest(emailAddress);
        try {
            String html = HtmlUtil.getOtpTemplate(otpRequest.getEmailAddress(), otpRequest.getOtp());
            sendEmail(otpRequest, "One-time passcode", html);
            otpRequest.setEmailSentSuccessfully(true);
        } catch (MessagingException e) {
            log.info("Failed to send game invite OTP email to '{}' with msg '{}'", otpRequest.getEmailAddress(), e);
            otpRequest.setEmailSentSuccessfully(false);
        }
        return otpRequest;
    }

    @CachePut(cacheNames = "otpCode", value = "otpCode", key = "#emailAddress")
    public OtpRequest sendRegisterOtp(String emailAddress) {
        log.info("Sending registration OTP email to '{}'", emailAddress);

        // Need to put in content ${HOST}.com/otp/${otp} and have that activate
        OtpRequest otpRequest = generateOtpRequest(emailAddress);
        try {
//            String html = HtmlUtil.getRegisterTemplate(otpRequest.getEmailAddress(), otpRequest.getOtp());
            String html = HtmlUtil.getOtpTemplate(otpRequest.getEmailAddress(), otpRequest.getOtp());
            sendEmail(otpRequest, "Register to play Top Chute!", html);
            otpRequest.setEmailSentSuccessfully(true);
        } catch (MessagingException e) {
            log.info("Failed to send register OTP email to '{}' with msg '{}'", otpRequest.getEmailAddress(), e);
            otpRequest.setEmailSentSuccessfully(false);
        }
        return otpRequest;
    }

    private void sendEmail(OtpRequest otpRequest, String subject, String html) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(otpRequest.getEmailAddress());
        helper.setSubject(subject);
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    @Cacheable(value = "otpCode", key = "#emailAddress")
    private OtpRequest generateOtpRequest(String emailAddress) {
        log.info("Generating OTP request for '{}'", emailAddress);
        int otp = generateOtp();
        return new OtpRequest(emailAddress, otp, false, Instant.now().plusSeconds(ttlSeconds));
    }

    private int generateOtp() {
        return secureRandom.nextInt((int) Math.pow(10, 6));
    }

}
