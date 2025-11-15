package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardPositionEntity;
import org.springframework.data.util.Pair;

public class TestApp {

    public static void main(String[] args) {
        GameBoardService gameBoardService = new GameBoardService();
        BoardPositionEntity position = BoardPositionEntity.builder().x(8).y(2).build();
        Pair<Integer, Integer> posNew = gameBoardService.getNewPlayerCoordinates(3, position, new int[10][5]);

        System.out.println("JIM " + Pair.of(0, 0));
        System.out.println("JIM " + posNew);
    }

}
