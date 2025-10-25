package io.github.conphucious.topchute.model;

import java.time.Instant;

public record User(String emailAddress, String name, Instant createdAt) {
}
