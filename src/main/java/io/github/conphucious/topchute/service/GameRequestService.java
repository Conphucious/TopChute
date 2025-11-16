package io.github.conphucious.topchute.service;

import io.github.conphucious.topchute.entity.BoardEntity;
import io.github.conphucious.topchute.entity.GameEntity;
import io.github.conphucious.topchute.entity.PlayerEntity;
import io.github.conphucious.topchute.entity.UserEntity;
import io.github.conphucious.topchute.model.BoardType;
import io.github.conphucious.topchute.model.GameStatus;
import io.github.conphucious.topchute.model.OtpRequest;
import io.github.conphucious.topchute.repository.GameRepository;
import io.github.conphucious.topchute.repository.UserRepository;
import io.github.conphucious.topchute.util.GameUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GameRequestService {

    private final OtpService otpService;
    private final UserRepository userRepository;

    private final BoardService boardService;
    private final UserService userService;
    private final PlayerService playerService;
    private final GameRepository gameRepository;

    @Autowired
    public GameRequestService(OtpService otpService, UserRepository userRepository, BoardService boardService,
                              UserService userService, PlayerService playerService, GameRepository gameRepository) {
        this.otpService = otpService;
        this.userRepository = userRepository;

        this.boardService = boardService;
        this.userService = userService;
        this.playerService = playerService;
        this.gameRepository = gameRepository;
    }

    public void inviteToGame(List<String> emailAddresses) {
        log.info("Sending invite to emails '{}'", emailAddresses);
        // check if user exists.

        List<String> emailsRegistered = emailAddresses.stream().filter(email -> userRepository.findById(email).isPresent()).toList();
        List<String> emailsNotRegistered = emailAddresses;
        emailAddresses.removeAll(emailsRegistered);

        log.info("ER '{}'\tNER '{}'", emailsRegistered, emailsNotRegistered);

        // Invite to game to registered users. Incl link
        sendInviteOtp(emailsRegistered);

        // Invite to register and join game. Incl link
        sendRegisterOtp(emailsNotRegistered);

        // Create associated ID to create new game with all players.

        // Must send out game request to all phone numbers.
        // Must check if user exists.
        // If yes, send invite to join
        // If no, send invite to register and auto join
        // Then create game
    }

    public void sendInviteOtp(List<String> emailAddresses) {
        Map<String, Boolean> emailSentSuccessfullyMap = emailAddresses.stream()
                .map(otpService::sendGameInvite)
                .collect(Collectors.toMap(OtpRequest::getEmailAddress, OtpRequest::isEmailSentSuccessfully));
    }

    public void sendRegisterOtp(List<String> emailAddresses) {
        Map<String, Boolean> emailSentSuccessfullyMap = emailAddresses.stream()
                .map(otpService::sendRegisterInvite)
                .collect(Collectors.toMap(OtpRequest::getEmailAddress, OtpRequest::isEmailSentSuccessfully));
    }

    public void registerUser() {

        // check if email exists. If so, give link to join game
        // If email not exists, give link to register and auto join game

//        Optional<UserEntity> userEntity = userRepository.findById(emailAddress);
//        if (userEntity.isPresent()) {
//            log.info("User already exists for '{}', skipping registration.", emailAddress);
//            return false;
//        }
    }

    public GameEntity createNewGame(int id) {
        // TODO : lookup ID and add all players. Hard coding for now
        // Look up users
        List<UserEntity> users = userService.fetchUsers(List.of("9phuc.nguyen6@gmail.com", "9phuc.nguyen9@gmail.com"));
        List<PlayerEntity> players = playerService.createPlayerEntity(users);
        BoardEntity board = boardService.createBoard(BoardType.DEFAULT, players);

        String uuid = GameUtil.uuid();
        GameEntity gameEntity = GameEntity.builder()
                .uuid(uuid)
                .status(GameStatus.NEW_GAME)
                .players(players)
                .board(board)
                .createdAt(Instant.now())
                .build();
        return gameRepository.save(gameEntity);
    }

}
