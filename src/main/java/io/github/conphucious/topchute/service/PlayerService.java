package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerEntity> createPlayerEntity(List<UserEntity> userEntities) {
        Instant timeUntilPlayerMove = generateNextMoveTime();
        List<PlayerEntity> playerEntities = userEntities.stream()
                .map(pe -> PlayerEntity.builder()
                        .user(pe)
                        .timeUntilPlayerCanMove(generateNextMoveTime())
                        .build())
                .toList();
        return playerRepository.saveAll(playerEntities);
    }

    private Instant generateNextMoveTime() {
        return Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
    }

}
