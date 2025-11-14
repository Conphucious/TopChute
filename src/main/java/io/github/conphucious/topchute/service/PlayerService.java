package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.repository.PlayerRepository;
import io.github.conphucious.topchute.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerEntity> createPlayerEntity(List<UserEntity> userEntities) {
        List<PlayerEntity> playerEntities = userEntities.stream()
                .map(pe -> PlayerEntity.builder()
                        .user(pe)
                        .timeUntilPlayerCanMove(TimeUtil.getNextMoveTime())
                        .build())
                .toList();
        return playerRepository.saveAll(playerEntities);
    }

}
