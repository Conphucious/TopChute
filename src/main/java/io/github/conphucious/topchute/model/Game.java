package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.model.map.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class Game {
    private final String uuid;
    private List<User> userList;
    private Board gameBoard;
    private final Instant createdAt;

    // Can be null
    private Instant endedAt;
}
