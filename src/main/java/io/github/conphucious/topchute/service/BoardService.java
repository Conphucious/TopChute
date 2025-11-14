package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.BoardType;
import io.github.conphucious.topchute.repository.BoardPositionRepository;
import io.github.conphucious.topchute.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private BoardPositionRepository boardPositionRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, BoardPositionRepository boardPositionRepository) {
        this.boardRepository = boardRepository;
        this.boardPositionRepository = boardPositionRepository;
    }

    public BoardEntity createBoard(BoardType boardType, List<PlayerEntity> players) {
        // Persist new BoardPositionEntity.
        List<BoardPositionEntity> boardPositionList = players.stream()
                .map(p -> BoardPositionEntity.builder().x(0).y(0).player(p).build())
                .toList();
        boardPositionList = boardPositionRepository.saveAll(boardPositionList);

        // Create player position map
        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = boardPositionList.stream()
                .collect(Collectors.toMap(BoardPositionEntity::getPlayer, b -> b));

        // Create board entity, add board position to entity.
        BoardEntity boardEntity = BoardEntity.builder().boardType(boardType).playerPositionMap(playerPositionMap).build();
        boardPositionList.forEach(bpe -> bpe.addBoard(boardEntity));

        // Save board entity and persist board position repository
        BoardEntity board = boardRepository.save(boardEntity);
        boardPositionRepository.saveAll(boardPositionList);
        return board;
    }

    public Optional<BoardEntity> getBoardById(int id) {
        return boardRepository.findById(id);
    }

    public List<BoardEntity> getBoardByIds(List<Integer> ids) {
        return boardRepository.findAllById(ids);
    }

}
