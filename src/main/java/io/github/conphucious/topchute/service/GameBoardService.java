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

        if (isOddRow) {
            moveAmount *= -1; // Reverse player direction
        }

        // Calculate new X position
        int playerXPositionNew = playerPositionX + moveAmount;
        int playerYPositionNew = playerPositionY;

        log.info("New calculated player position is [{},{}] with move amount '{}'.", playerXPositionNew, playerYPositionNew, moveAmount);

        // Check if out of bounds and adjust Y accordingly
        int xMax = board.length;
        if (isOddRow) {
            if (playerXPositionNew < 0) { // Moving left and needs to go to next floor.
                log.info("Player exceeded xMax moving left '{}'. Moving to next floor.", xMax);
                playerYPositionNew += 1;
                playerXPositionNew = Math.abs(playerXPositionNew);
            } else if (playerXPositionNew > xMax) { // Moving left and needs to go down a floor.
                log.info("Player subceeded xMax moving left '{}'. Moving to prior floor.", xMax);
                int deltaToMoveX = playerXPositionNew - xMax;
                playerYPositionNew -= 1;
                playerXPositionNew = deltaToMoveX;
            }
        } else {
            if (playerXPositionNew > xMax) { // Moving right and needs to go to next floor.
                log.info("Player exceeded xMax moving right '{}'. Moving to next floor.", xMax);
                int deltaToMoveX = playerXPositionNew - xMax;
                playerYPositionNew += 1;
                playerXPositionNew = deltaToMoveX;
            } else if (playerXPositionNew < 0) { // Moving left and needs to go down a floor.
                log.info("Player subceeded xMax moving right '{}'. Moving to prior floor.", xMax);
                int deltaToMoveX = xMax - Math.abs(playerXPositionNew);
                playerYPositionNew -= 1;
                playerXPositionNew = deltaToMoveX;
            }
        }

        log.info("Player final position is [{},{}].", playerXPositionNew, playerYPositionNew);

        // Check if reaches edge of board x and y then winner. Need to get exact number to win.
        return Pair.of(playerXPositionNew, playerYPositionNew);
    }

}
