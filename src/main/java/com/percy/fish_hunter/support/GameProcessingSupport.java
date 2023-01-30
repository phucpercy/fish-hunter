package com.percy.fish_hunter.support;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.percy.fish_hunter.converter.GameConverter;
import com.percy.fish_hunter.dto.GameResultDto;
import com.percy.fish_hunter.dto.PlayerDto;
import com.percy.fish_hunter.models.Game;
import com.percy.fish_hunter.models.GameStatus;
import com.percy.fish_hunter.models.RoomStatus;
import com.percy.fish_hunter.repository.GameRepository;
import com.percy.fish_hunter.repository.PlayerGameRepository;
import com.percy.fish_hunter.repository.RoomMemberRepository;
import com.percy.fish_hunter.repository.RoomRepository;
import com.percy.fish_hunter.service.GameService;
import com.percy.fish_hunter.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GameProcessingSupport {

    public static final int DEFAULT_INIT_TIME = 5 * 1000;
    public static final int DEFAULT_GAME_TIME = 60 * 1000;
    public static final int DEFAULT_READY_TIME = 10 * 1000;
    public static final int DEFAULT_TOTAL_PLAY_TIME = DEFAULT_GAME_TIME + DEFAULT_READY_TIME + DEFAULT_INIT_TIME;

    private final GameService gameService;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final GameRepository gameRepository;
    private final PlayerGameRepository playerGameRepository;
    private final SocketClientManagement socketClientManagement;
    private final SocketIOServer server;
    private final GameConverter gameConverter;
    private final PlayerService playerService;

    private void finishGame(Game game) {
        game.setStatus(GameStatus.FINISHED);
        gameRepository.save(game);
        computeGameResult(game);
        handleFinishGame(game);
    }

    private void handleFinishGame(Game game) {
        var room = roomRepository.getById(game.getRoomId());
        room.getRoomMembers().forEach(roomMember -> {
            roomMemberRepository.deleteRoomMemberByPrimaryKeyRoomIdAndPrimaryKeyPlayerId(room.getId(),
                roomMember.getPlayer().getId());
        });
        room.setRoomMembers(new ArrayList<>());
        room.setStatus(RoomStatus.WAITING);
        roomRepository.save(room);

        var playerGames = game.getPlayerGames();
        playerGames.forEach(playerGame -> {
            playerGame.setFinishDate(Date.from(Instant.now()));
            playerGameRepository.save(playerGame);

            var socketClient = socketClientManagement.getClientByPlayerId(playerGame.getPlayer().getId());
            if (socketClient != null) {
                socketClient.leaveRoom(String.valueOf(room.getId()));
                socketClient.joinRoom(SocketClientManagement.DEFAULT_GENERAL_ROOM);
            }
        });
        playerService.publishRoomMembersChangeEvent();
    }

    private void computeGameResult(Game game) {
        var gameDto = gameConverter.toDto(game);
        var playerGames = gameDto.getPlayers();
        var gameResult = new GameResultDto();
        gameResult.setDraw(true);
        var winner = playerGames.get(0);
        for (int i = 1; i < playerGames.size(); ++i) {
            if (playerGames.get(i).getPoint() > winner.getPoint()) {
                gameResult.setDraw(false);
                winner = playerGames.get(i);
            } else if (playerGames.get(i).getPoint() < winner.getPoint()) {
                gameResult.setDraw(false);
            }
        }
        if (!gameResult.isDraw()) {
            var winnerDto = new PlayerDto();
            winnerDto.setId(winner.getPlayer().getId());
            winnerDto.setUsername(winner.getPlayer().getUsername());
            gameResult.setWinner(winnerDto);
        }

        server.getRoomOperations(String.valueOf(game.getRoomId()))
            .sendEvent(SocketEventMessage.GAME_RESULT, gameResult);
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void handleGameTime() {
        var currentGames = gameRepository.findAllByStatus(GameStatus.IN_PROGRESS);
        var currentTimeEpoch = Instant.now().toEpochMilli();
        currentGames.forEach(game -> {
            Date createdAt = game.getCreatedDate();
            var passedTime = currentTimeEpoch - createdAt.toInstant().toEpochMilli();
            if (passedTime >= DEFAULT_TOTAL_PLAY_TIME) {
                finishGame(game);
            } else if (passedTime >= DEFAULT_READY_TIME + DEFAULT_INIT_TIME) {
                var timeLeft = (DEFAULT_TOTAL_PLAY_TIME - passedTime) / 1000;
                var gamePlayResponse = gameService.generateGamePlayResponse(game.getRoomId(), timeLeft);
                server.getRoomOperations(String.valueOf(game.getRoomId()))
                        .sendEvent(SocketEventMessage.GAME_PLAY, gamePlayResponse);
            } else if (passedTime >= DEFAULT_INIT_TIME && passedTime < DEFAULT_INIT_TIME + 1000) {
                var res = new ObjectMapper().createObjectNode();
                res.put("time", GameProcessingSupport.DEFAULT_READY_TIME / 1000);
                res.put("gameId", game.getId());

                server.getRoomOperations(String.valueOf(game.getRoomId())).sendEvent(SocketEventMessage.START_GAME, res);
            }
        });
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void handleAutoStartGame() {
        var nonEmptyRooms = roomRepository.findAllByStatus(RoomStatus.READY);
        nonEmptyRooms.forEach(room -> {
            if (room.getRoomMembers().size() == room.getRoomType().getNumberOfPlayerByType()) {
                room.setStatus(RoomStatus.IN_PROGRESS);
                roomRepository.save(room);
                Game newGame = new Game();
                newGame.setRoomId(room.getId());
                newGame.setStatus(GameStatus.IN_PROGRESS);
                gameRepository.save(newGame);

                room.getRoomMembers().forEach(
                        roomMember -> playerGameRepository.save(playerService.createNewPlayerInGame(roomMember.getPlayer(), newGame)));
            }
        });
    }

//    @Scheduled(fixedRate = 5 * 60 * 1000)
//    public void autoClearRoomMember() {
//
//    }
}
