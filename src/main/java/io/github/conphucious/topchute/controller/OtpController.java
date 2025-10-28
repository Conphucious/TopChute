package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.service.OtpService;
import io.github.conphucious.topchute.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("otp")
public class OtpController {

//    private final UserService otpService;
//
//    @Autowired
//    public OtpController(OtpService otpService) {
//        this.otpService = otpService;
//    }
//
//    @PostMapping("/{otp}")
//    public void activateAccount(@PathVariable String otp) {
//        otpService.activateAccount();
//    }

}
