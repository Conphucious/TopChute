package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.User;
import jakarta.persistence.Id;

import java.time.Instant;

public class PlayerEntity {

    @Id
    private int id;
    private User user;
    private Instant timeUntilPlayerCanMove;

}
