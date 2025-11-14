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

    public GameEntity createNewGame(int id) {
        // TODO : lookup ID and add all players. Hard coding for now
        // Look up users
        List<UserEntity> users = userService.fetchUsers(List.of("9phuc.nguyen6@gmail.com"));
        List<PlayerEntity> players = playerService.createPlayerEntity(users);
        BoardEntity board = boardService.createBoard(BoardType.DEFAULT, players);

        String uuid = GenerationUtil.uuid();
        GameEntity gameEntity = GameEntity.builder()
                .uuid(uuid)
                .players(players)
                .board(board)
                .createdAt(Instant.now())
                .build();
        return gameRepository.save(gameEntity);
    }

    public boolean isUserAvailableToMove() {
        return true;
    }

}
