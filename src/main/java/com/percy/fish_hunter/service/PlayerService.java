package com.percy.fish_hunter.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.percy.fish_hunter.converter.RoomConverter;
import com.percy.fish_hunter.dto.CommonDataInRoomDto;
import com.percy.fish_hunter.dto.RoomDto;
import com.percy.fish_hunter.models.*;
import com.percy.fish_hunter.repository.GameRepository;
import com.percy.fish_hunter.repository.PlayerGameRepository;
import com.percy.fish_hunter.repository.RoomMemberRepository;
import com.percy.fish_hunter.repository.RoomRepository;
import com.percy.fish_hunter.support.GameProcessingSupport;
import com.percy.fish_hunter.support.SocketClientManagement;
import com.percy.fish_hunter.support.SocketEventMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.percy.fish_hunter.support.SocketClientManagement.DEFAULT_GENERAL_ROOM;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class PlayerService {

    private final SocketIOServer server;

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final GameRepository gameRepository;
    private final PlayerGameRepository playerGameRepository;
    private final SocketClientManagement socketClientManagement;

    private final RoomConverter roomConverter;

    public List<RoomDto> findAllRooms() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        return roomConverter.toListDto(rooms);
    }

    private boolean isPlayerInRoom(Player player, Room room) {
        List<RoomMember> roomMembers = room.getRoomMembers();

        return roomMembers.stream().anyMatch(e -> e.getPlayer().equals(player));
    }

    public void publishRoomMembersChangeEvent() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        var res = roomConverter.toListDto(rooms);
        server.getAllClients().forEach(client -> client.sendEvent(SocketEventMessage.ROOM_MEMBERS_CHANGED, res));
    }

    private void handleAutoStartGame(Room room) {
        if (room.getRoomMembers().size() == room.getRoomType().getNumberOfPlayerByType()) {

            Game newGame = new Game();
            newGame.setRoomId(room.getId());
            newGame.setStatus(GameStatus.IN_PROGRESS);
            gameRepository.save(newGame);

            room.getRoomMembers().forEach(
                roomMember -> playerGameRepository.save(createNewPlayerInGame(roomMember.getPlayer(), newGame)));

            var res = new ObjectMapper().createObjectNode();
            res.put("time", GameProcessingSupport.DEFAULT_READY_TIME / 1000);
            res.put("gameId", newGame.getId());

            server.getRoomOperations(String.valueOf(room.getId())).sendEvent(SocketEventMessage.START_GAME, res);
        }
    }

    private void joinSocketServerByRoom(Room room, Player player) {
        var socketClient = socketClientManagement.getClientByPlayerId(player.getId());
        if (socketClient != null) {
            socketClient.leaveRoom(DEFAULT_GENERAL_ROOM);
            socketClient.joinRoom(String.valueOf(room.getId()));
        }
    }

    private RoomMember createNewMemberInRoom(Player player, Room room) {
        RoomMember roomMember = new RoomMember();

        roomMember.setRoom(room);
        roomMember.setPlayer(player);

        return roomMember;
    }

    private PlayerGame createNewPlayerInGame(Player player, Game game) {
        PlayerGame playerGame = new PlayerGame();

        playerGame.setGame(game);
        playerGame.setPlayer(player);

        return playerGame;
    }

    public RoomDto join(RoomDto roomDto, Player player) throws Exception {
        Room room = roomRepository.findOneById(roomDto.getId());

        if (!room.getStatus().equals(RoomStatus.WAITING) || isPlayerInRoom(player, room)
            || room.getRoomMembers().size() >= room.getRoomType().getNumberOfPlayerByType()) {
            throw new Exception("This room not available!");
        }

        RoomMember roomMember = roomMemberRepository.save(createNewMemberInRoom(player, room));

        Room entity = roomMember.getRoom();
        entity.getRoomMembers().add(roomMember);

        publishRoomMembersChangeEvent();
        joinSocketServerByRoom(room, player);

        Room newRoom = roomRepository.findOneById(roomDto.getId());
        handleAutoStartGame(newRoom);

        return roomConverter.toDto(entity);
    }

    public void transferCommonDataForMemberInRoom(CommonDataInRoomDto data) {

        Room room = roomRepository.findOneById(data.getRoomId());
        List<RoomMember> roomMembers = room.getRoomMembers();
        RoomMember roomMember = roomMemberRepository.findOneByPrimaryKeyPlayerIdOrderByCreatedDateDesc(
            data.getPlayerId());

        if (roomMembers.contains(roomMember)) {
            roomMembers.forEach(member -> {
                if (!member.equals(roomMember)) {
                    server.getRoomOperations(String.valueOf(room.getId()))
                        .sendEvent(SocketEventMessage.COMMON_DATA, data);
                }
            });
        }
    }

    public RoomDto leave(RoomDto roomDto, Player player) {
        return null;
    }
}
