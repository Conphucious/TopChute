package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.Player;
import io.github.conphucious.topchute.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.Table;

import java.util.Map;

@Entity
@Table(name = "board")
public class BoardEntity {

    @Id
    private int id;

    @MapKey(name = "Player")
    private Map<Player, Integer[][]> playerPositionMap;

    @MapKey(name = "UserEntity")
    private Map<User, Player> userPlayerMap;
}
