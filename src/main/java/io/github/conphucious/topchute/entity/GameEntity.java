package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.model.map.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "game")
public class GameEntity {

    @Id
    private String guid;

    // Many to Many
    private List<UserEntity> userEntityList;

    // One to Many
    private BoardEntity gameBoard;

    private Instant createdAt;

    private Instant endedAt;
}
