package io.github.conphucious.topchute.entity;

import jakarta.persistence.Id;

import java.time.Instant;
import java.util.List;

public class GameRequestEntity {

    @Id
    private int id;

    // Need separate tables
    private List<UserEntity> user; // TODO : this needs to be like "requested user entity". If user exists, use else use that.

    private boolean hasAcceptedInvite;

    private Instant createdAt;

}
