package io.github.conphucious.topchute.model;

import java.time.Instant;

public record User(String phoneNumber, String name, Instant createdAt) {
}
