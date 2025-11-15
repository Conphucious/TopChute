package io.github.conphucious.topchute.model;

import io.github.conphucious.topchute.entity.GameEntity;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class GameResponse {

    private GameResponseDetail detail;

    private GameEntity gameEntity;


}
