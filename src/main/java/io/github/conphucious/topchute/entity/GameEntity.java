package io.github.conphucious.topchute.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "tc_game")
public class GameEntity {

    @Id
    private String uuid;

    @ManyToMany
    @JoinTable(
            name = "tc_game_users",
            joinColumns = @JoinColumn(name = "game_guid"),
            inverseJoinColumns = @JoinColumn(name = "user_email_address")
    )
    private List<UserEntity> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private BoardEntity board;

    @CreationTimestamp
    private Instant createdAt;

    private Instant endedAt;

}
