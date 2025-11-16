package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.BoardAction;
import io.github.conphucious.topchute.model.GameEvent;
import io.github.conphucious.topchute.model.GameEventType;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.model.GameStatus;
import io.github.conphucious.topchute.repository.BoardRepository;
import io.github.conphucious.topchute.repository.GameRepository;
import io.github.conphucious.topchute.util.GameUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Log4j2
@Service
public class GameEventService {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameEventService(BoardService boardService, BoardRepository boardRepository, GameRepository gameRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.gameRepository = gameRepository;
    }

    public GameResponse.GameResponseBuilder performPlayerAction(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse) {
        int spacesToMove = GameUtil.generateRandomInt(6);

        // Check if selected for event. Hardcoded to 10% chance for now.
        boolean isRandomlySelectedForEvent = GameUtil.isRngSelected(100, 10);
        if (isRandomlySelectedForEvent) {
            return performRngEvent(game, player, gameResponse, spacesToMove);
        }

        // Neutral event means normal move.
        GameEvent gameEvent = GameEvent.builder()
                .boardAction(BoardAction.MOVE_FORWARD)
                .gameEventType(GameEventType.MOVEMENT)
                .moveAmount(spacesToMove)
                .player(player)
                .build();
        gameResponse.gameEvent(gameEvent);
        gameResponse.detail(GameResponseDetail.PLAYER_MOVING);

        // Move player progressively
        movePlayerAndCheckWinCondition(game, player, gameResponse);

        return gameResponse;
    }

    // TODO : introduce random events where you can teleport around board
    // TODO : get values of RNG from yaml conf
    // TODO Generate event details like "So so stopped you. You move back X spaces"
    private GameResponse.GameResponseBuilder performRngEvent(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse, int spacesToMove) {
        log.info("RNG event triggered for player '{}' on game '{}'", player.getUser().getEmailAddress(), game.getUuid());
        gameResponse.detail(GameResponseDetail.EVENT_TRIGGERED);

        // Hardcoded 65 % chance for moving backwards. 35% chance for not moving at all.
        boolean isMovingBackwards = GameUtil.isRngSelected(100, 65);
        GameEvent gameEvent = GameEvent.builder()
                .gameEventType(GameEventType.RNG_EVENT)
                .boardAction(isMovingBackwards
                        ? BoardAction.MOVE_BACKWARD
                        : BoardAction.SKIP_TURN)
                .moveAmount(isMovingBackwards
                        ? spacesToMove * -1
                        : 0)
                .player(player)
                .build();
        gameResponse.gameEvent(gameEvent);

        // Skip turn
        if (!isMovingBackwards) {
            log.info("Player '{}' is skipping turn on game '{}'", player.getUser().getEmailAddress(), game.getUuid());
            gameResponse.gameEvent(gameEvent);
            return gameResponse;
        }

        // Move player regressively
        movePlayerAndCheckWinCondition(game, player, gameResponse);

        return gameResponse;
    }

    private void movePlayerAndCheckWinCondition(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse) {
        BoardPositionEntity playerBoardPosition = game.getBoard().getPlayerPositionMap().get(player);
        Pair<Integer, Integer> playerCoordinateCurrent = Pair.of(playerBoardPosition.getX(), playerBoardPosition.getY());
        GameResponse temp = gameResponse.build();
        log.info("Player '{}' pending action is '{}' moving '{}' spaces",
                player.getUser().getEmailAddress(), temp.getGameEvent().getBoardAction(), temp.getGameEvent().getMoveAmount());
        Pair<Integer, Integer> playerCoordinateNew = boardService.movePlayer(game, player, temp.getGameEvent().getMoveAmount());

        // Player needs to land EXACTLY on winning tile. Else report if they moved past this.
        boolean hasPlayerMovedPastWinningCondition = temp.getGameEvent().getGameEventType() == GameEventType.MOVEMENT
                && playerCoordinateCurrent.getFirst().equals(playerCoordinateNew.getFirst())
                && playerCoordinateCurrent.getSecond().equals(playerCoordinateNew.getSecond());
        if (hasPlayerMovedPastWinningCondition) {
            gameResponse.detail(GameResponseDetail.PLAYER_MOVING_OVERFLOW);
        }

        // Update player position, board, game.
        savePlayerMovement(game, player, playerCoordinateNew);

        // Check win condition
        boolean hasPlayerWon = boardService.isUserOnWinningTile(playerBoardPosition, game.getBoard().getBoardType());
        log.info("Player '{}' has won: '{}'", player.getUser().getEmailAddress(), hasPlayerWon);
        if (hasPlayerWon) {
            game.setStatus(GameStatus.COMPLETED);
            game.setWinner(player);
            game.setEndedAt(Instant.now());
            gameRepository.save(game);
            gameResponse.detail(GameResponseDetail.PLAYER_WON);
        }
    }

    private void savePlayerMovement(GameEntity game, PlayerEntity player, Pair<Integer, Integer> playerCoordinateNew) {
        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = game.getBoard().getPlayerPositionMap();

        // Set new player position
        BoardPositionEntity playerBoardPosition = playerPositionMap.get(player);
        playerBoardPosition.setX(playerCoordinateNew.getFirst());
        playerBoardPosition.setY(playerCoordinateNew.getSecond());

        // Save to board
        BoardEntity board = game.getBoard();
        board.setPlayerPositionMap(playerPositionMap);
        boardRepository.save(board);

        // Save to game
        game.getBoard().setPlayerPositionMap(playerPositionMap);
        gameRepository.save(game);
    }

}
