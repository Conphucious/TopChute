package io.github.conphucious.topchute.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

    public void createNewGame() {
        String guid = String.valueOf(UUID.randomUUID());

//        new Game()
    }

    public boolean isUserAvailableToMove() {
        return true;
    }

}
