package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.util.HtmlUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendGameInvite(OtpRequest otpRequest) throws MessagingException {
        String subject = "One-time passcode";
        String html = HtmlUtil.getOtpTemplate(otpRequest.getEmailAddress(), otpRequest.getOtp());
        sendEmail(otpRequest, subject, html);
    }

    public void sendRegisterInvite(OtpRequest otpRequest) throws MessagingException {
        String subject = "Register to play Top Chute!";
        String html = HtmlUtil.getRegisterTemplate(otpRequest.getEmailAddress(), "http://google.com/");
        sendEmail(otpRequest, subject, html);
    }

    private void sendEmail(OtpRequest otpRequest, String subject, String html) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(otpRequest.getEmailAddress());
        helper.setSubject(subject);
        helper.setText(html, true);
        javaMailSender.send(message);
    }

}
