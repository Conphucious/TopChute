package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.user.UserActivationDto;
import io.github.conphucious.topchute.dto.user.UserDto;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("activation/{otp}")
    public ResponseEntity<UserActivationDto> activateAccount(@PathVariable int otp, @Valid @RequestBody UserDto userDto) {
        UserActivationDto userActivationDto = userService.activateUser(userDto, otp);
        return Optional.ofNullable(userActivationDto.getFailureReason()).isEmpty()
                ? ResponseEntity.ok(userActivationDto)
                : ResponseEntity.internalServerError().body(userActivationDto);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        if (userService.isUserRegistered(userDto)) {
            log.info("User registration request with DTO '{}' already exists.", userDto);
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("User already exists.");
        }

        UserEntity user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{emailAddress}")
    public ResponseEntity<UserEntity> fetchUser(@Valid @Email @PathVariable String emailAddress) {
        Optional<UserEntity> user = userService.fetchUser(emailAddress);
        String logMsg = user.isPresent()
                ? "User found for "
                : "User not found for ";
        log.info("{}'{}'", logMsg, emailAddress);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{emailAddress}")
    public ResponseEntity<Object> modifyUserName(@Valid @Email @PathVariable String emailAddress, @RequestBody String name) {
        Optional<UserEntity> user = userService.updateUserName(emailAddress, name);
        if (user.isEmpty()) {
            String msg = "User with email '" + emailAddress + "' does not exist for modification";
            log.info(msg);
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok(user.get());
    }

}
