package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.dto.core.GameActionDto;
import io.github.conphucious.topchute.dto.core.GameActionDtoType;
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
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Log4j2
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameEventService gameEventService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, GameEventService gameEventService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.gameEventService = gameEventService;
    }

    public boolean isGameCompleted(String uuid) {
        Optional<GameEntity> gameEntity = gameRepository.findById(uuid);
        return gameEntity.map(game -> game.getStatus() == GameStatus.COMPLETED).orElse(false);
    }

    public GameResponse performAction(String uuid, GameActionDto gameActionDto) {
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
        if (gameActionDto.getActionType() == GameActionDtoType.END_GAME) {
            return endGame(game, gameResponse);
        }

        // Game is completed
        GameStatus status = game.getStatus();
        if (status == GameStatus.COMPLETED) {
            log.warn("Cannot complete game action. Game is '{}' for uuid '{}'", status, uuid);
            return gameResponse.detail(GameResponseDetail.GAME_COMPLETED).build();
        }

        // Find user
        String emailAddress = gameActionDto.getEmailAddress();
        Optional<PlayerEntity> playerEntity = playerRepository.findByUserEmailAddress(emailAddress);
        // TODO : And where game guid = uuid

        // player does not exist or exist in game
        if (playerEntity.isEmpty() || !game.getPlayers().contains(playerEntity.get())) {
            log.warn("Player '{}' does not exist in game with uuid '{}'", emailAddress, uuid);
            return GameResponse.builder().detail(GameResponseDetail.PLAYER_NOT_EXIST).build();
        }

        // Player cannot move because of cooldown
        PlayerEntity player = playerEntity.get();
        Instant timeNow = Instant.now();
//        Instant timeUntilPlayerCanMove = player.getTimeUntilPlayerCanMove();

        Instant timeUntilPlayerCanMove = timeNow.minusSeconds(50000); // FOR DEBUGGING

        if (timeUntilPlayerCanMove.compareTo(timeNow) >= 0) {
            Duration duration = Duration.between(timeNow, timeUntilPlayerCanMove);
            log.warn("Player '{}' cannot move yet. Cooldown time remaining for move '{}.'", emailAddress, duration);
            return GameResponse.builder().detail(GameResponseDetail.PLAYER_MOVE_COOLDOWN).build();
        }

        gameResponse = gameEventService.performPlayerAction(game, player, gameResponse);

        // set player cooldown
        player.setTimeUntilPlayerCanMove(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS));
        playerRepository.save(player);

        return gameResponse.build();
    }

    public GameResponse endGame(GameEntity game, GameResponse.GameResponseBuilder gameResponse) {
        Instant timeNow = Instant.now();
        log.info("Ending game '{}' at {}.", game.getUuid(), timeNow);
        game.setEndedAt(timeNow);
        game.setStatus(GameStatus.COMPLETED);
        return gameResponse.detail(GameResponseDetail.ENDING_GAME).build();
    }

}
