package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.model.Player;
import io.github.conphucious.topchute.model.User;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DefaultBoard extends Board {

    public DefaultBoard(List<User> participatingUsers) {
        super(participatingUsers);
    }

}
