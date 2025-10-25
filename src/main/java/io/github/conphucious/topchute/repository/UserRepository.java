package io.github.conphucious.topchute.repository;

import io.github.conphucious.topchute.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
