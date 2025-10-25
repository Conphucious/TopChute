package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.map.BoardPosition;
import io.github.conphucious.topchute.model.map.BoardType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import java.util.Map;

@Entity
@Table(name = "tc_board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BoardType boardType;

    @ElementCollection
    @CollectionTable(name = "tc_player_position", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "id")
    @Column(name = "player_position_map")
    private Map<PlayerEntity, BoardPositionEntity> playerPositionMap;

}
