package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.core.GameActionDto;
import io.github.conphucious.topchute.dto.core.GameActionDtoType;
import io.github.conphucious.topchute.dto.core.GameRequestDto;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.service.GameRequestService;
import io.github.conphucious.topchute.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRequestService gameRequestService;
    private final GameService gameService;

    @Autowired
    public GameController(GameRequestService gameRequestService, GameService gameService) {
        this.gameRequestService = gameRequestService;
        this.gameService = gameService;
    }

    @PostMapping("/request")
    public ResponseEntity<Object> requestGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        gameRequestService.inviteToGame(gameRequestDto.getEmailAddressList());
        return ResponseEntity.accepted().body("Game request sent");
    }

    @PostMapping("/request/{id}")
    public ResponseEntity<GameEntity> createGame(@PathVariable int id) {
        GameEntity gameEntity = gameRequestService.createNewGame(id);
        // TODO : get rid of json ignore and tostring on entities and create dto to transfer back to resp
        return ResponseEntity.ok(gameEntity);
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<GameResponse> performAction(@PathVariable String uuid, @RequestBody GameActionDto gameActionDto) {
        // Game is already completed. Don't do anything
        if (gameService.isGameCompleted(uuid)) {
            return ResponseEntity.badRequest()
                    .body(GameResponse.builder()
                            .detail(GameResponseDetail.GAME_COMPLETED)
                            .build());
        }

        // Game is aborted
        if (gameActionDto.getActionType() == GameActionDtoType.END_GAME) {
            boolean isGameEnded = gameService.endGame(uuid);
            return !isGameEnded
                    ? ResponseEntity.notFound().build()
                    : ResponseEntity.ok().build();
        }

        // Check if game is completed or aborted here.
        GameResponse gameResponse = gameService.performAction(uuid, gameActionDto);
//        GameEntity gameEntity = gameService.createNewGame(id);
        // TODO : get rid of json ignore and tostring on entities and create dto to transfer back to resp
        return ResponseEntity.ok(gameResponse);
    }

}
