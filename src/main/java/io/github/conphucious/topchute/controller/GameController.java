package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.core.GameActionDto;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.service.GameService;
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

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
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

        // TODO : game is aborted

        // Check if game is completed or aborted here.
        GameResponse gameResponse = gameService.performAction(uuid, gameActionDto);
//        GameEntity gameEntity = gameService.createNewGame(id);
        // TODO : get rid of json ignore and tostring on entities and create dto to transfer back to resp
        return ResponseEntity.ok(gameResponse);
    }

}
