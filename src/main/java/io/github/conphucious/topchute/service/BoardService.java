package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.model.map.Board;
import io.github.conphucious.topchute.model.map.BoardTile;
import io.github.conphucious.topchute.model.map.BoardType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class BoardService {

    public Board createBoard(BoardType boardType, List<User> participatingUsers) {
        Board board = null;
        if (boardType == BoardType.DEFAULT) {
            File imgFile = new File("");
            BoardTile[][] boardTile = new BoardTile[10][6];
            board = new Board(boardType, imgFile, boardTile, participatingUsers);
        }

        return board;
    }

}
