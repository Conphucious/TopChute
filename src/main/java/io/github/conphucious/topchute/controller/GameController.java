package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.GameActionDto;
import io.github.conphucious.topchute.dto.UserDto;
import io.github.conphucious.topchute.model.Game;
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

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{id}")
    public ResponseEntity createGame(@PathVariable int id) {
        gameService.createNewGame(id);
        return null;
    }

//    Action controller

//    @PostMapping("/action/{id}")
//    public ResponseEntity<Game> movePiece(@Valid @RequestBody UserDto userDto, GameActionDto gameRequestDto) {
//        System.out.println(gameRequestDto);
//        return null;
//    }

}
