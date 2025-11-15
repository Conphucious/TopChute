package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.dto.core.GameActionDto;
import io.github.conphucious.topchute.dto.core.GameActionType;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.model.GameResponse;
import io.github.conphucious.topchute.model.GameResponseDetail;
import io.github.conphucious.topchute.model.GameStatus;
import io.github.conphucious.topchute.repository.GameRepository;
import io.github.conphucious.topchute.repository.PlayerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public GameResponse action(String uuid, GameActionDto gameActionDto) {
        GameResponse.GameResponseBuilder gameResponse = GameResponse.builder();
        Optional<GameEntity> gameEntity = gameRepository.findById(uuid);

        // Game doesn't exist
        if (gameEntity.isEmpty()) {
            log.warn("Game was not found for uuid '{}'", uuid);
            return gameResponse.detail(GameResponseDetail.GAME_NOT_FOUND).build();
        }

        GameEntity game = gameEntity.get();
        gameResponse.gameEntity(game);

        // End game
        if (gameActionDto.getActionType() == GameActionType.END_GAME) {
            return endGame(game, gameResponse);
        }

        // Game is completed
        GameStatus status = game.getStatus();
        if (GameStatus.isGameEndedStatus(status)) {
            log.warn("Cannot complete game action. Game is '{}' for uuid '{}'", status, uuid);
            return gameResponse.detail(GameResponseDetail.GAME_COMPLETED).build();
        }

        // Find user
        String emailAddress = gameActionDto.getEmailAddress();
        Optional<PlayerEntity> playerEntity = playerRepository.findByUserEmailAddress(emailAddress);

        // player does not exist or exist in game
        if (playerEntity.isEmpty() || !game.getPlayers().contains(playerEntity.get())) {
            log.warn("Player '{}' does not exist in game with uuid '{}'", emailAddress, uuid);
            return GameResponse.builder().detail(GameResponseDetail.PLAYER_NOT_EXIST).build();
        }

        // Player cannot move because of cooldown
        PlayerEntity player = playerEntity.get();
        Instant timeNow = Instant.now();
        Instant timeUntilPlayerCanMove = player.getTimeUntilPlayerCanMove();
        if (timeUntilPlayerCanMove.compareTo(timeNow) >= 0) {
            Duration duration = Duration.between(timeUntilPlayerCanMove, timeNow);
            log.warn("Player '{}' cannot move yet. Cooldown time remaining for move '{}'", emailAddress, duration);
            return GameResponse.builder().detail(GameResponseDetail.PLAYER_MOVE_COOLDOWN).build();
        }

        // do action

        System.out.println(gameEntity);

        return gameResponse.build();
    }

    public GameResponse endGame(GameEntity game, GameResponse.GameResponseBuilder gameResponse) {
        game.setEndedAt(Instant.now());
        game.setStatus(GameStatus.COMPLETED);
        return gameResponse.build();
    }


}
