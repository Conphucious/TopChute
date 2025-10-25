package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.model.Player;
import io.github.conphucious.topchute.model.User;

import java.util.List;
import java.util.Map;

public class DefaultBoard implements Board {
    private BoardTile[][] boardTile;
    private Map<User, Integer[][]> playerPositionMap;
    private List<User> userList;
}
