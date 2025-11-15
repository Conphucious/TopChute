package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.GameEvent;
import io.github.conphucious.topchute.model.GameEventType;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.util.GenerationUtil;
import org.springframework.stereotype.Service;

@Service
public class GameEventService {
    public GameResponse movePlayer(GameEntity game, PlayerEntity player, GameResponse.GameResponseBuilder gameResponse) {
        // Check if selected for event. Hardcoded to 15% chance for now.
        boolean isRandomlySelectedForEvent = GenerationUtil.isRngSelected(100, 15);
        if (isRandomlySelectedForEvent) {
            return performEvent(game, gameResponse);
        }

        GameEventType gameEventType = GameEventType.NEUTRAL;

        // Perform event
        // Find game x, y boundary.
        // Move player based on boundary
        // If at end, end game. Set winner.
        // If event, do event and set what happened

        return gameResponse.build();
    }

    // TODO : get values of RNG from yaml conf
    private GameResponse performEvent(GameEntity game, GameResponse.GameResponseBuilder gameResponse) {
        gameResponse.detail(GameResponseDetail.EVENT_TRIGGERED);

        // Hardcoded 65 % chance for good event. 35% chance for bad event.
        boolean isGoodEvent = GenerationUtil.isRngSelected(100, 65);

        // Mark what kind of event happened
        GameEventType gameEventType = isGoodEvent
                ? GameEventType.GOOD
                : GameEventType.BAD;

        if (isGoodEvent) {
            // Perform good event
        } else {
            // Perform bad event
        }

        gameResponse.gameEvent(
                GameEvent.builder()
                        .gameEventType(gameEventType)
//                        .boardAction()
                        .build()
        );

        return gameResponse.build();
    }
}
