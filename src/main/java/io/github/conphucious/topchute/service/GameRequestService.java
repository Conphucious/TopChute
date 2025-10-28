package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GameRequestService {

    private final OtpService otpService;
    private final UserRepository userRepository;

    @Autowired
    public GameRequestService(OtpService otpService, UserRepository userRepository) {
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    public void inviteToGame(List<String> emailAddresses) {
        log.info("Sending invite to emails '{}'", emailAddresses);
        // check if user exists.

        List<String> emailsRegistered = emailAddresses.stream().filter(email -> userRepository.findById(email).isPresent()).toList();
        List<String> emailsNotRegistered = emailAddresses;

        log.info("ER '{}'\tNER '{}'", emailsRegistered, emailsNotRegistered);
        emailAddresses.removeAll(emailsRegistered);

        // Invite to game to registered users. Incl link
        sendInviteOtp(emailsRegistered);

        // Invite to register and join game. Incl link
        sendRegisterOtp(emailsNotRegistered);

        // Must send out game request to all phone numbers.
        // Must check if user exists.
        // If yes, send invite to join
        // If no, send invite to register and auto join
        // Then create game
    }

    public void sendInviteOtp(List<String> emailAddresses) {
        Map<String, Boolean> emailSentSuccessfullyMap = emailAddresses.stream()
                .map(otpService::sendGameInviteOtp)
                .collect(Collectors.toMap(OtpRequest::getEmailAddress, OtpRequest::isEmailSentSuccessfully));
    }

    public void sendRegisterOtp(List<String> emailAddresses) {
        Map<String, Boolean> emailSentSuccessfullyMap = emailAddresses.stream()
                .map(otpService::sendRegisterOtp)
                .collect(Collectors.toMap(OtpRequest::getEmailAddress, OtpRequest::isEmailSentSuccessfully));
    }

    public void registerUser() {

        // check if email exists. If so, give link to join game
        // If email not exists, give link to register and auto join game

//        Optional<UserEntity> userEntity = userRepository.findById(emailAddress);
//        if (userEntity.isPresent()) {
//            log.info("User already exists for '{}', skipping registration.", emailAddress);
//            return false;
//        }
    }

}
