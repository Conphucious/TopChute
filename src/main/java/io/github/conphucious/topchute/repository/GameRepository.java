package io.github.conphucious.topchute.repository;

import io.github.conphucious.topchute.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, String> {
}
