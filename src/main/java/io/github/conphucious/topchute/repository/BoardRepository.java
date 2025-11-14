package io.github.conphucious.topchute.repository;

import io.github.conphucious.topchute.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
}
