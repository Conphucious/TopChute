package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.map.BoardType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Entity
@Table(name = "tc_board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BoardType boardType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    @MapKeyJoinColumn(name = "player_id")
    private Map<PlayerEntity, BoardPositionEntity> playerPositionMap = new HashMap<>();

    @CreationTimestamp
    private Instant createdAt;

}
