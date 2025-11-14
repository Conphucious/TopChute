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
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
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
    private Map<PlayerEntity, BoardPositionEntity> playerPositionMap;

    @CreationTimestamp
    private Instant createdAt;

    public void addPlayerPositionMap(Map<PlayerEntity, BoardPositionEntity> playerPositionMap) {
        this.playerPositionMap = playerPositionMap;
    }

}
