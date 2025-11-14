package io.github.conphucious.topchute.controller;

import io.github.conphucious.topchute.model.BoardType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("board")
public class BoardController {

    @GetMapping("/type")
    public ResponseEntity<BoardType[]> fetchBoardType() {
        return ResponseEntity.ok(BoardType.values());
    }

}
