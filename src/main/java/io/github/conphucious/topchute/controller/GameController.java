package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/{id}")
    public ResponseEntity<GameEntity> createGame(@PathVariable int id) {
        GameEntity gameEntity = gameService.createNewGame(id);
        // TODO : get rid of json ignore and tostring on entities and create dto to transfer back to resp
        return ResponseEntity.ok(gameEntity);
    }

//    Action controller

//    @PostMapping("/action/{id}")
//    public ResponseEntity<Game> movePiece(@Valid @RequestBody UserDto userDto, GameActionDto gameRequestDto) {
//        System.out.println(gameRequestDto);
//        return null;
//    }

}
