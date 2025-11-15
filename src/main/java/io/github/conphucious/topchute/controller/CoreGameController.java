package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.GameRequestDto;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.service.GameRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game/request")
public class CoreGameController {

    private final GameRequestService gameRequestService;

    @Autowired
    public CoreGameController(GameRequestService gameRequestService) {
        this.gameRequestService = gameRequestService;
    }

    @PostMapping
    public ResponseEntity<Object> requestGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        gameRequestService.inviteToGame(gameRequestDto.getEmailAddressList());
        return ResponseEntity.accepted().body("Game request sent");
    }

    @PostMapping("/{id}")
    public ResponseEntity<GameEntity> createGame(@PathVariable int id) {
        GameEntity gameEntity = gameRequestService.createNewGame(id);
        // TODO : get rid of json ignore and tostring on entities and create dto to transfer back to resp
        return ResponseEntity.ok(gameEntity);
    }

}
