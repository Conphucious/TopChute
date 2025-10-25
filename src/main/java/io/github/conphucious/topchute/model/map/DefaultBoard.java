package io.github.conphucious.topchute.model.map;

import io.github.conphucious.topchute.model.User;
import lombok.Getter;

import java.util.List;

@Getter
public class DefaultBoard extends Board {

    public DefaultBoard(List<User> participatingUsers) {
        super(participatingUsers);
    }

}
