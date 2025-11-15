package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.BoardType;
import io.github.conphucious.topchute.repository.GameRepository;
import io.github.conphucious.topchute.util.GenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class GameService {

    private final BoardService boardService;
    private final UserService userService;
    private final PlayerService playerService;
    private final GameRepository gameRepository;


    @Autowired
    public GameService(BoardService boardService, UserService userService, PlayerService playerService, GameRepository gameRepository) {
        this.boardService = boardService;
        this.userService = userService;
        this.playerService = playerService;
        this.gameRepository = gameRepository;
    }


}
