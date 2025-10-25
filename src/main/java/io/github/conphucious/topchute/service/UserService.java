package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.dto.UserDto;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
