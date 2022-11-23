package com.percy.fish_hunter.support;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Getter
public class SocketClientManagement {

    public static final String DEFAULT_GENERAL_ROOM = "general_room";

    private Map<Integer, SocketIOClient> clientMap = new HashMap<>();

    public void addConnectedClient(Integer playerId, SocketIOClient client) {
        this.clientMap.putIfAbsent(playerId, client);
    }

    public SocketIOClient removeDisconnectedClient(Integer playerId) {
        return this.clientMap.remove(playerId);
    }

    public SocketIOClient getClientByPlayerId(Integer playerId) {
        return this.clientMap.get(playerId);
    }
}
