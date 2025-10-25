package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.dto.GameDto;
import io.github.conphucious.topchute.model.Game;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    @PostMapping
    public ResponseEntity<Game> createNewGame(@Valid @RequestBody GameDto gameDto) {
        System.out.println(gameDto);
        return null;
    }

}
