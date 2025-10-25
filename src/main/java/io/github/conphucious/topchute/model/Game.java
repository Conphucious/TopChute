package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.model.map.Board;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
public class Game {
    private String guid;
    private List<User> userList;
    private Board gameBoard;
    private Instant createdAt;
    private Instant endedAt;
}
