package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.entity.GameEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameResponse {

    private GameResponseDetail detail;

    private GameEntity gameEntity;

    private GameEvent gameEvent;

}
