package com.percy.fish_hunter.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.percy.fish_hunter.dto.RoomDto;
import com.percy.fish_hunter.models.Player;
import com.percy.fish_hunter.service.PlayerService;
import com.percy.fish_hunter.support.SocketEventMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RoomController {

    private PlayerService playerService;
    private SocketIOServer server;

    @PostMapping("/room/join")
    public ResponseEntity<Object> join(@RequestBody RoomDto roomDto) {
        try {
            Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            RoomDto response = playerService.join(roomDto, player);
            server.getRoomOperations(String.valueOf(response.getId()))
                    .sendEvent(SocketEventMessage.JOIN_GAME, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<Object> getAllRooms() {
        var rooms = playerService.findAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/room/leave")
    public ResponseEntity<Object> leaveRoom(@RequestBody RoomDto roomDto) {
        try {
            Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            RoomDto response = playerService.leave(roomDto, player);
            server.getRoomOperations(String.valueOf(response.getId()))
                    .sendEvent(SocketEventMessage.JOIN_GAME, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
