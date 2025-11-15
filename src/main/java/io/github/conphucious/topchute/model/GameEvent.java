package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.entity.PlayerEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameEvent {

    private PlayerEntity player;

    private BoardAction boardAction;

    private GameEventType gameEventType;

    private int moveAmount;

}
