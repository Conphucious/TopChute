package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.BoardPositionEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // Create into player entities
        Map<PlayerEntity, BoardPositionEntity> playerEntityBoardPositionEntityMap = new HashMap<>();

        for (UserEntity userEntity : userEntities) {
            BoardEntity boardEntity = BoardEntity.builder().boardType(BoardType.DEFAULT).build();
            boardService.createBoard()

            BoardPositionEntity boardPositionEntity = BoardPositionEntity.builder().x(0).y(0).board(boardEntity).build();
            playerEntityBoardPositionEntityMap.put(userEntity, boardPositionEntity);
        }

        BoardEntity boardEntity = BoardEntity.builder().boardType(BoardType.DEFAULT).playerPositionMap().build();

        Map<PlayerEntity, BoardPositionEntity> playerEntityBoardPositionEntityMap = userEntities.stream().collect(Collectors.toMap(u -> u, BoardPositionEntity.builder().x(0).y(0).board(boardEntity)));

        // set initial player positions
        boardPositionEntityMap.put(userEntities);

        // build obard

        BoardEntity boardEntity = BoardEntity.builder().boardType(BoardType.DEFAULT).playerPositionMap().build();
//        List<User> userList = userEntities.stream().map(UserEntity::toUser).toList();
//        Board board = boardService.createBoard(BoardType.DEFAULT, userList);

        String uuid = GenerationUtil.uuid();
        Instant createdAt = Instant.now();

        GameEntity gameEntity = new GameEntity();
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
