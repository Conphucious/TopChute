package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.model.Player;
import io.github.conphucious.topchute.model.User;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DefaultBoard implements Board {
    private final File imgFile;
    private final BoardTile[][] boardTile;
    private final Map<Player, Integer[][]> playerPositionMap;
    private final Map<User, Player> userPlayerMap;

    public DefaultBoard(List<User> participatingUsers) {
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
