package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.model.map.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class Game {
    private String uuid;
    private List<User> userList;
    private Board gameBoard;
    private Instant createdAt;
    private Instant endedAt;
}
