package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.Game;
import io.github.conphucious.topchute.model.User;
import io.github.conphucious.topchute.model.map.Board;
import io.github.conphucious.topchute.model.map.BoardType;
import io.github.conphucious.topchute.repository.GameRepository;
import io.github.conphucious.topchute.util.GenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final BoardService boardService;
    private final GameRepository gameRepository;

    @Autowired
    public GameService(BoardService boardService, GameRepository gameRepository) {
        this.boardService = boardService;
        this.gameRepository = gameRepository;
    }

    public void createNewGame(int id) {
        // TODO : lookup ID and add all players. Hard coding for now
        // Also has playerl ist and board type

        // Look up users
        List<UserEntity> userEntities = List.of(new UserEntity("9phuc.nguyen6@gmail.ocm", "me"));
        List<User> userList = userEntities.stream().map(UserEntity::toUser).toList();
        Board board = boardService.createBoard(BoardType.DEFAULT, userList);

        String uuid = GenerationUtil.uuid();
        Instant createdAt = Instant.now();
        Game game = Game.builder()
                .uuid(uuid)
                .userList(userList)
                .gameBoard(board)
                .createdAt(createdAt)
                .build();

        // TODO : persist

        // Need to make BoardEntity
        String gameEntityUuid = GenerationUtil.uuid();
        GameEntity gameEntity = new GameEntity(gameEntityUuid, userEntities, boardEntity);

        gameRepository.save();


    }

    public boolean isUserAvailableToMove() {
        return true;
    }

}
