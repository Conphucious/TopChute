package io.github.conphucious.topchute.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
    private final String phoneNumber;
    private final String name;
}
