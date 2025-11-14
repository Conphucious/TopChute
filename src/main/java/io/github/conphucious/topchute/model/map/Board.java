package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.BoardPositionEntity;
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
    private final Map<Player, BoardPosition> playerPositionMap;
    private final Map<User, Player> userPlayerMap;
    private final BoardType boardType;

    public Board(BoardType boardType, File imgFile, BoardTile[][] boardTile, List<User> participatingUsers) {
        this.boardType = boardType;
        this.imgFile = imgFile;
        this.boardTile = boardTile;
        playerPositionMap = new HashMap<>();
        userPlayerMap = new HashMap<>();

        for (User user : participatingUsers) {
            Player player = new Player(user);
            playerPositionMap.put(player, new BoardPosition(0, 0));
            userPlayerMap.put(user, player);
        }
    }

    public BoardEntity toEntity() {
        // need to get board
//        BoardPositionEntity.builder().x(x).y(y).board();
//        return BoardEntity
//                .builder()
//                .boardType(getBoardType())
//                .playerPositionMap()
//                .build();
    }

}
