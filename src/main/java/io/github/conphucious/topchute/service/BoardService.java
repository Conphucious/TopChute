package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.model.map.Board;
import io.github.conphucious.topchute.model.map.BoardTile;
import io.github.conphucious.topchute.model.map.BoardType;
import io.github.conphucious.topchute.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardEntity createBoard(BoardType boardType, List<PlayerEntity> participatingUsers) {
        BoardEntity.builder().boardType(boardType).playerPositionMap();
        boardRepository.save()

        Board board = null;
        if (boardType == BoardType.DEFAULT) {
            File imgFile = new File("");
            BoardTile[][] boardTile = new BoardTile[10][6];
            board = new Board(boardType, imgFile, boardTile, participatingUsers);
        }

        return board;
    }

}
