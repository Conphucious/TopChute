package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.GameRequestDto;
import io.github.conphucious.topchute.service.GameRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game/request")
public class GameRequestController {

    private final GameRequestService gameRequestService;

    @Autowired
    public GameRequestController(GameRequestService gameRequestService) {
        this.gameRequestService = gameRequestService;
    }

    @PostMapping
    public ResponseEntity<Object> requestGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        gameRequestService.inviteToGame(gameRequestDto.getEmailAddressList());
        return ResponseEntity.accepted().body("Game request sent");
    }

}
