package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.BoardType;
import io.github.conphucious.topchute.repository.BoardPositionRepository;
import io.github.conphucious.topchute.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BoardService {

    // TODO : every time you pass a player, you push them back X spaces at random chance.

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

    public int[][] determineBoardSize(BoardType boardType) {
        if (boardType == BoardType.DEFAULT) {
            return new int[10][1];
        }
        throw new IllegalArgumentException("Unsupported board type: " + boardType);
    }

    public Pair<Integer, Integer> movePlayer(GameEntity game, PlayerEntity player, int spacesToMove) {
        BoardType boardType = game.getBoard().getBoardType();
        int[][] board = determineBoardSize(boardType);

        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = game.getBoard().getPlayerPositionMap();
        BoardPositionEntity playerBoardPosition = playerPositionMap.get(player);

        return getNewPlayerCoordinates(spacesToMove, playerBoardPosition, board);
    }

    /*
    Assumption that player will never be inbound on bad coordinates (out of bounds).
     */
    public Pair<Integer, Integer> getNewPlayerCoordinates(int spacesToMove, BoardPositionEntity playerBoardPosition, int[][] board) {
        int playerPositionX = playerBoardPosition.getX();
        int playerPositionY = playerBoardPosition.getY();
        int moveAmount = spacesToMove;
        log.info("Current player position is [{},{}] with move amount '{}'.", playerPositionX, playerPositionY, moveAmount);

        // Even number -> move right | Odd number -> move left
        boolean isOddRow = (playerBoardPosition.getY() % 2 != 0);
        log.info("Player is moving left is '{}'.", isOddRow);

        // Reverse player direction to left if odd row. First row is 0 (even) always moves right.
        moveAmount = isOddRow
                ? moveAmount * -1
                : moveAmount;

        // Calculate new X position
        int playerXPositionNew = playerPositionX + moveAmount;
        log.info("New unadjusted calculated player position is [{},{}] with move amount '{}'.",
                playerXPositionNew, playerPositionY, moveAmount);

        // Check if out of bounds and adjust Y accordingly
        int xMax = board.length;
        log.info("Checking boundaries on board with xMax,yMax of [{},{}].", xMax, board[0].length);
        Pair<Integer, Integer> playerCoordinates = isOddRow
                ? getPlayerCoordinatesOddRow(xMax, playerXPositionNew, playerPositionY)
                : getPlayerCoordinatesEvenRow(xMax, playerXPositionNew, playerPositionY);
        log.info("New adjusted calculated position is [{},{}].", playerCoordinates.getFirst(), playerCoordinates.getSecond());

        // Check Y boundaries
        int yMax = board[0].length;
        if (playerCoordinates.getSecond() > yMax) {
            log.info("Player exceeded yMax moving '{}'. Resetting player to prior position [{},{}].",
                    board[0].length, playerPositionX, playerPositionY);
            playerCoordinates = Pair.of(playerPositionX, playerPositionY);
        } else if (playerCoordinates.getSecond() < 0) {
            log.info("Player subceeded yMin moving '0'. Resetting player to default position [{},{}].",
                    0, 0);
            playerCoordinates = Pair.of(0, 0);
        }

        // Check if reaches edge of board x and y then winner. Need to get exact number to win. Caller will do this.
        return playerCoordinates;
    }

    /**
     * If Y is odd then winning X position is 0
     * If Y is even then winning X position is xMax
     */
    public boolean isUserOnWinningTile(BoardPositionEntity boardPositionEntity, BoardType boardType) {
        int[][] board = determineBoardSize(boardType);
        int xMax = board.length;
        int yMax = board[0].length;
        boolean isOdd = yMax % 2 != 0;
        log.info("Checking to see if player at winning tile at position [{},{}] on board with xMax,yMax of [{},{}].",
                boardPositionEntity.getX(), boardPositionEntity.getY(), xMax, yMax);

        return isOdd
                ? boardPositionEntity.getX() == 0 && boardPositionEntity.getY() == yMax
                : boardPositionEntity.getX() == xMax && boardPositionEntity.getY() == yMax;
    }

    private Pair<Integer, Integer> getPlayerCoordinatesOddRow(int xMax, int playerPositionX, int playerPositionY) {
        int x = playerPositionX;
        int y = playerPositionY;

        if (x < 0) { // Moving left and needs to go to next floor.
            log.info("Player exceeded xMax moving left '{}'. Moving to next floor.", xMax);
            x = Math.abs(x);
            y += 1;
        } else if (x > xMax) { // Moving left and needs to go down a floor.
            log.info("Player subceeded xMax moving left '{}'. Moving to prior floor.", xMax);
            x = xMax - (x - xMax);
            y -= 1;
        }
        return Pair.of(x, y);
    }

    private Pair<Integer, Integer> getPlayerCoordinatesEvenRow(int xMax, int playerPositionX, int playerPositionY) {
        int x = playerPositionX;
        int y = playerPositionY;

        if (x > xMax) { // Moving right and needs to go to next floor.
            log.info("Player exceeded xMax moving right '{}'. Moving to next floor.", xMax);
            x -= xMax;
            y += 1;
        } else if (x < 0) { // Moving left and needs to go down a floor.
            log.info("Player subceeded xMax moving right '{}'. Moving to prior floor.", xMax);
            x = xMax - Math.abs(x);
            y -= 1;
        }
        return Pair.of(x, y);
    }

}
