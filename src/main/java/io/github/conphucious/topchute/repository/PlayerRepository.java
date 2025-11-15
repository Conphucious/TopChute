package io.github.conphucious.topchute.repository;

import io.github.conphucious.topchute.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {

    Optional<PlayerEntity> findByUserEmailAddress(String emailAddress);

}
