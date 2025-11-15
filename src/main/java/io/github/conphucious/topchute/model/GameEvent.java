package io.github.conphucious.topchute.model;

import lombok.Builder;

@Builder
public class GameEvent {

    private BoardAction boardAction;
    private GameEventType gameEventType;

}
