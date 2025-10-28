package io.github.conphucious.topchute.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@ConfigurationProperties
@Component
public class EmailConf {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean isAuthEnabled;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean isTlsEnabled;

    @Value("${spring.mail.properties.mail.debug.enable}")
    private boolean isDebugEnabled;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", isAuthEnabled);
        props.put("mail.smtp.starttls.enable", isTlsEnabled);
        props.put("mail.debug", isDebugEnabled);

        return mailSender;
    }

}
