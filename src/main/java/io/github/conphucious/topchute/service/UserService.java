package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.dto.UserDto;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.repository.UserRepository;
import io.github.conphucious.topchute.util.CacheUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final CacheManager cacheManager;

    @Autowired
    public UserService(UserRepository userRepository, OtpService otpService, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.cacheManager = cacheManager;
    }

    public User createUser(UserDto userDto) {
        log.info("Registering user '{}'", userDto);
        UserEntity userEntity = userRepository.save(
                UserEntity.builder()
                        .emailAddress(userDto.getEmailAddress())
                        .name(userDto.getName())
                        .createdAt(Instant.now())
                        .build());
        return new User(userEntity.getEmailAddress(), userEntity.getName(), userEntity.getCreatedAt());
    }

    public Optional<User> fetchUser(String emailAddress) {
        log.info("Fetching user with email '{}'", emailAddress);
        Optional<UserEntity> userEntity = userRepository.findById(emailAddress);
        return userEntity.map(entity -> new User(entity.getEmailAddress(), entity.getName(), entity.getCreatedAt()));
    }

    public boolean isUserRegistered(UserDto userDto) {
        return userRepository.findById(userDto.getEmailAddress()).isPresent();
    }

    public Optional<User> updateUserName(String emailAddress, String name) {
        Optional<UserEntity> userEntity = userRepository.findById(emailAddress);
        if (userEntity.isEmpty()) {
            return Optional.empty();
        }
        UserEntity modifiedUserEntity = userEntity.get();
        modifiedUserEntity.setName(name);
        return Optional.of(new User(modifiedUserEntity.getEmailAddress(),
                modifiedUserEntity.getName(),
                modifiedUserEntity.getCreatedAt()));

    }

    public boolean activateUser(UserDto userDto, int otp) {
        log.info("Activating user '{}'", userDto);
        // TODO : Need to include reason and see if evict
        Cache cache = cacheManager.getCache(CacheUtil.OTP_CODE);
        if (cache == null) {
            log.warn("Cache '{}' is null", CacheUtil.OTP_CODE);
            return false;
        }
        Optional<OtpRequest> otpRequest = CacheUtil.retrieveOtpRequestFromOtpCodeCache(userDto.getEmailAddress(), cache);
        if (otpRequest.isEmpty()) {
            log.info("No OTP request found in cache for '{}'", userDto.getEmailAddress());
            return false;
        }

        Instant timeNow = Instant.now();
        if (timeNow.isAfter(otpRequest.get().getExpiresAt())) {
            log.info("Cache evicted for '{}' due to expiration of '{}' while time now is '{}'.", userDto.getEmailAddress(), otpRequest.getExpiresAt(), timeNow);
            return false;
        }

        boolean isUserOtpValid = otpRequest != null && otpRequest.get().getOtp() == otp;
        if (isUserOtpValid) {
            cache.evict(userDto.getEmailAddress());
            log.info("User activation OTP successful for '{}'", userDto.getEmailAddress());
        } else {
            log.info("User activation OTP failed for '{}'. OTP code did not match!", userDto.getEmailAddress());
        }

        return isUserOtpValid;
    }
}
