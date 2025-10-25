package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.UserDto;
import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        if (userService.isUserRegistered(userDto)) {
            log.info("User registration request with DTO '{}' already exists.", userDto);
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("User already exists.");
        }

        User user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<User> fetchUser(@Valid @Pattern(regexp = "\\d{10}") @PathVariable String phoneNumber) {
        Optional<User> user = userService.fetchUser(phoneNumber);
        String logMsg = user.isPresent()
                ? "User found for "
                : "User not found for ";
        log.info("{}'{}'", logMsg, phoneNumber);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{phoneNumber}")
    public ResponseEntity<Object> modifyUserName(@Valid @Pattern(regexp = "\\d{10}") @PathVariable String phoneNumber, @RequestBody String name) {
        Optional<User> user = userService.updateUserName(phoneNumber, name);
        if (user.isEmpty()) {
            String msg = "User of PN '" + phoneNumber + "' does not exist for modification";
            log.info(msg);
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok(user.get());
    }

}
