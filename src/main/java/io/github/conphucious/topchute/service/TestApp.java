package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardPositionEntity;
import org.springframework.data.util.Pair;

public class TestApp {

    public static void main(String[] args) {
        GameBoardService gameBoardService = new GameBoardService();
        BoardPositionEntity position = BoardPositionEntity.builder().x(9).y(1).build();
        Pair<Integer, Integer> posNew = gameBoardService.getNewPlayerCoordinates(-2, position, new int[10][4]);

        // 9, 1 moving left
        // -2 is move right twice
        // 9, 0

//        boolean isWinning = gameBoardService.isUserOnWinningTile(position, BoardType.DEFAULT);

//        System.out.println("JIM " + Pair.of(0, 0));
        System.out.println("JIM " + posNew);

        posNew = gameBoardService.getNewPlayerCoordinates(-2, BoardPositionEntity.builder().x(9).y(3).build(), new int[10][4]);
        System.out.println("JIM " + posNew);

//        System.out.println("WIN: " + isWinning);
    }

}
