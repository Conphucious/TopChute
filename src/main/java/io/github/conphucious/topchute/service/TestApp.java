package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.model.BoardType;
import org.springframework.data.util.Pair;

public class TestApp {

    public static void main(String[] args) {
        GameBoardService gameBoardService = new GameBoardService();
        BoardPositionEntity position = BoardPositionEntity.builder().x(10).y(4).build();
        Pair<Integer, Integer> posNew = gameBoardService.getNewPlayerCoordinates(0, position, new int[10][5]);

        boolean isWinning = gameBoardService.isUserOnWinningTile(position, BoardType.DEFAULT);

//        System.out.println("JIM " + Pair.of(0, 0));
//        System.out.println("JIM " + posNew);

        System.out.println("WIN: " + isWinning);
    }

}
