package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.BoardType;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
public class GameBoardService {

    // TODO : every time you pass a player, you push them back X spaces at random chance.

    public void movePlayer(GameEntity game, PlayerEntity player, int spacesToMove) {
        BoardType boardType = game.getBoard().getBoardType();
        int[][] board = getBoardSize(boardType);

        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = game.getBoard().getPlayerPositionMap();
        BoardPositionEntity playerBoardPosition = playerPositionMap.get(player);

        Pair<Integer, Integer> playerNewCoordinates = getNewPlayerCoordinates(spacesToMove, playerBoardPosition, board);

        // Even number -> move right so do nothing
        // Odd number -> move left reverse direction
        int moveAmount = spacesToMove;
        if (playerBoardPosition.getX() % 2 != 0) {
            moveAmount *= -1;
        }

        // Check if reaches edge of board x and y then winner. Need to get exact number to win.

        int xMax = board.length;
        int yMax = board[0].length;

        // If odd and reaches 0 then reset and move next floor
        // If even and reaches n then reset and move next floor


        // If spaces to move is negative then go backwards

        // Update player position.
    }

    public int[][] getBoardSize(BoardType boardType) {
        if (boardType == BoardType.DEFAULT) {
            return new int[10][4];
        }
        throw new IllegalArgumentException("Unsupported board type: " + boardType);
    }

    // If even, position moves forward -> and starts at 0
    // If odd, position moves down <- and starts at last index n

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
        log.info("New unadjusted calculated player position is [{},{}] with move amount '{}'.", playerXPositionNew, playerPositionY, moveAmount);


        // TODO : need to check if exceeds Y
        // Check if out of bounds and adjust Y accordingly
        int xMax = board.length;
        log.info("Checking boundaries on board with xMax,yMax of [{},{}].", xMax, board[0].length);
        Pair<Integer, Integer> playerCoordinates = isOddRow
                ? getPlayerCoordinatesOddRow(xMax, playerXPositionNew, playerPositionY)
                : getPlayerCoordinatesEvenRow(xMax, playerXPositionNew, playerPositionY);
        log.info("New adjusted calculated position is [{},{}].", playerCoordinates.getFirst(), playerCoordinates.getSecond());

        int yMax = board[0].length;
        if (isPlayerExceedingCoordinatesY(yMax, playerCoordinates)) {
            log.info("Player exceeded yMax moving '{}'. Resetting player to prior position [{},{}].",
                    board[0].length, playerPositionX, playerPositionY);
            playerCoordinates = Pair.of(playerPositionX, playerPositionY);
        }

        // Check if reaches edge of board x and y then winner. Need to get exact number to win. Caller will do this.
        return playerCoordinates;
    }

    /**
     * If Y is odd then winning X position is 0
     * If Y is even then winning X position is xMax
     *
     * @param boardPositionEntity
     * @param boardType
     * @return
     */
    public boolean isUserOnWinningTile(BoardPositionEntity boardPositionEntity, BoardType boardType) {
        int[][] board = getBoardSize(boardType);
        int xMax = board.length;
        int yMax = board[0].length;
        boolean isOdd = yMax % 2 != 0;

        log.info("Checking to see if player at winning tile at position [{},{}] on board with xMax,yMax of [{},{}].",
                boardPositionEntity.getX(), boardPositionEntity.getY(), xMax, yMax);

        return isOdd
                ? boardPositionEntity.getX() == 0 && boardPositionEntity.getY() == yMax
                : boardPositionEntity.getX() == xMax && boardPositionEntity.getY() == yMax;
    }

    private boolean isPlayerExceedingCoordinatesY(int yMax, Pair<Integer, Integer> playerCoordinates) {
        return playerCoordinates.getSecond() > yMax;
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
            x = y - xMax;
            y -= 1;
        }
        return Pair.of(x, y);
    }

    private Pair<Integer, Integer> getPlayerCoordinatesEvenRow(int xMax, int playerPositionX, int playerPositionY) {
        int x = playerPositionX;
        int y = playerPositionY;

        if (playerPositionX > xMax) { // Moving right and needs to go to next floor.
            log.info("Player exceeded xMax moving right '{}'. Moving to next floor.", xMax);
            x = playerPositionX - xMax;
            y += 1;
        } else if (playerPositionX < 0) { // Moving left and needs to go down a floor.
            log.info("Player subceeded xMax moving right '{}'. Moving to prior floor.", xMax);
            x = xMax - Math.abs(playerPositionX);
            y -= 1;
        }
        return Pair.of(x, y);
    }

}
