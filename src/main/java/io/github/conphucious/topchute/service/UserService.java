package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.dto.UserActivationDto;
import io.github.conphucious.topchute.dto.UserDto;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.ActivationFailureReason;
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

    public UserActivationDto activateUser(UserDto userDto, int otp) {
        log.info("Activating user '{}'", userDto);

        // Retrieve from cache
        Cache cache = cacheManager.getCache(CacheUtil.OTP_CODE);
        Optional<OtpRequest> otpRequest = CacheUtil.retrieveOtpRequestFromOtpCodeCache(cache, userDto.getEmailAddress());
        Optional<ActivationFailureReason> failureReason = validateFailureReason(cache, otpRequest.orElse(null), userDto.getEmailAddress());

        if (failureReason.isPresent()) {
            return UserActivationDto.builder()
                    .user(userDto)
                    .otpCode(otp)
                    .failureReason(failureReason.orElse(null))
                    .build();
        }

        boolean isUserOtpValid = otpRequest.get().getOtp() == otp;
        if (isUserOtpValid) {
            cache.evict(userDto.getEmailAddress());
            log.info("User activation OTP successful for '{}'", userDto.getEmailAddress());
            return UserActivationDto.builder()
                    .user(userDto)
                    .otpCode(otp)
                    .build();
        }

        log.info("User activation OTP failed for '{}'. OTP code did not match!", userDto.getEmailAddress());
        return UserActivationDto.builder()
                .user(userDto)
                .otpCode(otp)
                .failureReason(ActivationFailureReason.OTP_CODE_NOT_MATCH)
                .build();
    }

    private Optional<ActivationFailureReason> validateFailureReason(Cache cache, OtpRequest otpRequest, String emailAddressKey) {
        Instant timeNow = Instant.now();
        if (Optional.ofNullable(cache).isEmpty()) {
            log.info("Nothing exists in the OTP code cache.");
            return Optional.of(ActivationFailureReason.OTP_CODE_NOT_EXISTS);
        } else if (otpRequest == null) {
            log.info("No OTP request found in cache for '{}'", emailAddressKey);
            return Optional.of(ActivationFailureReason.OTP_CODE_NOT_EXISTS);
        } else if (timeNow.isAfter(otpRequest.getExpiresAt())) {
            log.info("OTP expired for '{}' due to expiration time of '{}' while time now is '{}'. Evicting record from cache!",
                    otpRequest.getEmailAddress(), otpRequest.getExpiresAt(), timeNow);
            return Optional.of(ActivationFailureReason.OTP_CODE_EXPIRED);
        }
        return Optional.empty();
    }

}
