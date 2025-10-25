package io.github.conphucious.topchute.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Player {
    private final User user;
    private Instant timeUntilPlayerCanMove;

    public Player(User user) {
        this.user = user;
        timeUntilPlayerCanMove = Instant.now();
    }
}
