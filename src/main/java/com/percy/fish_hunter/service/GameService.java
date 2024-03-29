package com.percy.fish_hunter.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.percy.fish_hunter.converter.PlayerGameConverter;
import com.percy.fish_hunter.dto.AddPointDto;
import com.percy.fish_hunter.dto.FishAssetResponse;
import com.percy.fish_hunter.repository.PlayerGameRepository;
import com.percy.fish_hunter.support.SocketEventMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GameService {

    private static int MAX_SCREEN_WIDTH = 1920;
    private static int MAX_SCREEN_HEIGHT = 1080;
    private static final Random random = new Random();

    private final SocketIOServer server;
    private final PlayerGameRepository playerGameRepository;
    private final PlayerGameConverter playerGameConverter;

    private int generateRandomNumberInRange(int min, int max) {
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public FishAssetResponse generateFishAsset() {
        var fishLevel = generateRandomNumberInRange(1, 13);
        var angle = (double) generateRandomNumberInRange(0, MAX_SCREEN_WIDTH);
        var x = generateRandomNumberInRange(0, MAX_SCREEN_WIDTH);
        var y = generateRandomNumberInRange(0, MAX_SCREEN_HEIGHT);

        x = x < MAX_SCREEN_WIDTH / 2 ? 0 : MAX_SCREEN_WIDTH;
        angle = Math.atan2(y - MAX_SCREEN_HEIGHT / 2, x - MAX_SCREEN_WIDTH / 2) * 180 / Math.PI;

        if (angle < 45 && angle >= -45) {  //right
            angle = generateRandomNumberInRange(135, 225);
        } else if (angle < 135 && angle >= 45) {  //down
            angle = generateRandomNumberInRange(-45, -135);
        } else if ((angle <= 180 && angle >= 135) || (angle >= -180 && angle < -135)) {  //left
            angle = generateRandomNumberInRange(-45, 45);
        } else if (angle <= -45 && angle >= -135) {  //up
            angle = generateRandomNumberInRange(45, 135);
        }
        angle = Math.toRadians(angle);
        var vx = Math.cos(angle);
        var vy = Math.sin(angle);

        return FishAssetResponse.builder()
                .x(x)
                .y(y)
                .vx(vx)
                .vy(vy)
                .angle(angle)
                .level(fishLevel)
                .maxWidth(MAX_SCREEN_WIDTH)
                .maxHeight(MAX_SCREEN_HEIGHT)
                .build();
    }

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

    public Collection<FishAssetResponse> generateSeriesFishAsset(int amount) {
        Collection<FishAssetResponse> res = new ArrayList<>();

        for (int i = 0; i < amount; ++i) {
            res.add(generateFishAsset());
        }

        return res;
    }
}
