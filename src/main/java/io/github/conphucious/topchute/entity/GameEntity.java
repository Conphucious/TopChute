package io.github.conphucious.topchute.entity;

import io.github.conphucious.topchute.model.GameStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tc_game")
public class GameEntity {

    @Id
    private String uuid;

    @Setter
    private GameStatus status;

    @ManyToMany
    @JoinTable(
            name = "tc_game_users",
            joinColumns = @JoinColumn(name = "game_guid"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerEntity> players;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private BoardEntity board;

    @CreationTimestamp
    private Instant createdAt;

    @Setter
    private Instant endedAt;

}
