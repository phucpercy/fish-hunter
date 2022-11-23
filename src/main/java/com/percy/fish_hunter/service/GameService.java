package com.percy.fish_hunter.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.percy.fish_hunter.converter.PlayerGameConverter;
import com.percy.fish_hunter.dto.AddPointDto;
import com.percy.fish_hunter.repository.PlayerGameRepository;
import com.percy.fish_hunter.support.SocketEventMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GameService {

    private final SocketIOServer server;
    private final PlayerGameRepository playerGameRepository;
    private final PlayerGameConverter playerGameConverter;

    public void addPoint(AddPointDto addPointDto) {
        var playerGame = playerGameRepository
                .findOneByPrimaryKeyGameIdAndPrimaryKeyPlayerIdAndFinishDateIsNull(
                        addPointDto.getGameId(),
                        addPointDto.getPlayerId()
                );
        if (playerGame != null) {
            playerGame.setPoint(playerGame.getPoint() + addPointDto.getPoint());
            playerGameRepository.save(playerGame);

            var res = playerGameConverter.toDto(playerGame);
            server.getRoomOperations(String.valueOf(addPointDto.getRoomId())).sendEvent(SocketEventMessage.POINT_CHANGED, res);
        }
    }
}
