package com.percy.fish_hunter.listenner;

import com.corundumstudio.socketio.annotation.OnEvent;
import com.percy.fish_hunter.dto.AddPointDto;
import com.percy.fish_hunter.dto.CommonDataInRoomDto;
import com.percy.fish_hunter.service.GameService;
import com.percy.fish_hunter.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class PlayerMessageEventHandler {

    private final @NonNull PlayerService playerService;
    private final @NonNull GameService gameService;

    @OnEvent("commonData")
    public void onEvent(CommonDataInRoomDto data) {
        playerService.transferCommonDataForMemberInRoom(data);
    }

    @OnEvent("point")
    public void onEvent(AddPointDto addPointDto) {
        gameService.addPoint(addPointDto);
    }
}
