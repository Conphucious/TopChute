package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.model.Player;
import io.github.conphucious.topchute.model.User;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Board {
    private final File imgFile; // pop from json
    private final BoardTile[][] boardTile; // populate from json
    private final Map<Player, Integer[][]> playerPositionMap;
    private final Map<User, Player> userPlayerMap;

    public Board(List<User> participatingUsers) {
        imgFile = new File("");
        boardTile = new BoardTile[10][6];
        playerPositionMap = new HashMap<>();
        userPlayerMap = new HashMap<>();

        for (User user : participatingUsers) {
            Player player = new Player(user);
            playerPositionMap.put(player, new Integer[0][0]);
            userPlayerMap.put(user, player);
        }
    }
}
