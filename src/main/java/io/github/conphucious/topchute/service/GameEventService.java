package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.GameEvent;
import io.github.conphucious.topchute.model.GameEventType;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.util.GenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameEventService {

    private final GameBoardService gameBoardService;

    @Autowired
    public GameEventService(GameBoardService gameBoardService) {
        this.gameBoardService = gameBoardService;
    }

    public GameResponse performPlayerAction(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse) {
        // Check if selected for event. Hardcoded to 15% chance for now.
        boolean isRandomlySelectedForEvent = GenerationUtil.isRngSelected(100, 15);
        if (isRandomlySelectedForEvent) {
            return performEvent(game, player, gameResponse);
        }

        GameEventType gameEventType = GameEventType.NEUTRAL;

        // Perform event
        // Find game x, y boundary.
        // Move player based on boundary
        // If at end, end game. Set winner.
        // If event, do event and set what happened

        return gameResponse.build();
    }

    // TODO : introduce random events where you can teleport around board
    // TODO : get values of RNG from yaml conf
    private GameResponse performEvent(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse) {
        gameResponse.detail(GameResponseDetail.EVENT_TRIGGERED);

        // Hardcoded 65 % chance for good event. 35% chance for bad event.
        boolean isGoodEvent = GenerationUtil.isRngSelected(100, 65);

        // Mark what kind of event happened
        GameEventType gameEventType = isGoodEvent
                ? GameEventType.GOOD
                : GameEventType.BAD;

        int spacesToMove = GenerationUtil.generateRandomInt(5);
        // If good, instant move X, if bad instant move back X

        Pair<Integer, Integer> playerCoordinateNew = gameBoardService.movePlayer(game, player, spacesToMove);

        // Update player position, board, game.
        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = game.getBoard().getPlayerPositionMap();
        BoardPositionEntity playerBoardPosition = playerPositionMap.get(player);
        playerBoardPosition.setX(playerCoordinateNew.getFirst());
        playerBoardPosition.setY(playerCoordinateNew.getSecond());

        game.getBoard().setPlayerPositionMap(playerPositionMap);
        boardRepository.save(playerBoardPosition);
        gameRepository.save(game);

        // add to game response new player coordinates, spaces moved?
        // Update player position.
        // Update game
        // update board
        // Check win condition


        // check if end game condition
        gameResponse.gameEvent(
                GameEvent.builder()
                        .gameEventType(gameEventType)
//                        .boardAction()
                        .build()
        );

        return gameResponse.build();
    }

    private void movePlayer(GameEntity game, PlayerEntity player, int spacesToMove) {
        game.getBoard().getBoardType(); // With defined board types, we have certain specs. Certain bad x,y

        // Update player position.
        Map<PlayerEntity, BoardPositionEntity> playerPositionMap = game.getBoard().getPlayerPositionMap();
    }
}
