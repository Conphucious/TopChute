package io.github.conphucious.topchute.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("otp")
public class OtpController {

    @PostMapping("/{otp}")
    public void activateAccount(@PathVariable String otp) {

    }

}
