package com.percy.fish_hunter.listenner;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.percy.fish_hunter.converter.RoomConverter;
import com.percy.fish_hunter.repository.RoomMemberRepository;
import com.percy.fish_hunter.repository.RoomRepository;
import com.percy.fish_hunter.service.GameService;
import com.percy.fish_hunter.support.SocketClientManagement;
import com.percy.fish_hunter.support.SocketEventMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.percy.fish_hunter.support.SocketClientManagement.DEFAULT_GENERAL_ROOM;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class MessageEventHandler {

	private static final String HANDSHAKE_DATA_PLAYER_PARAM = "playerId";

	private final SocketIOServer server;
	private final SocketClientManagement socketClientManagement;
	private final RoomRepository roomRepository;
	private final RoomConverter roomConverter;
	private final RoomMemberRepository roomMemberRepository;
	private final GameService gameService;

	@OnConnect
	public void onconnect(SocketIOClient client) {
		if (client.getHandshakeData().getSingleUrlParam(HANDSHAKE_DATA_PLAYER_PARAM) == null) {
			client.disconnect();
		}
		int playerId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam(HANDSHAKE_DATA_PLAYER_PARAM));
		var currentRoom = roomMemberRepository.findOneByPrimaryKeyPlayerIdOrderByCreatedDateDesc(playerId);
		if (currentRoom != null) {
			client.joinRoom(String.valueOf(currentRoom.getRoom().getId()));
		} else {
			client.joinRoom(DEFAULT_GENERAL_ROOM);
		}
		socketClientManagement.addConnectedClient(playerId, client);
	}

	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		int playerId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam(HANDSHAKE_DATA_PLAYER_PARAM));
		gameService.handleInGameDisconnectRoomMember(playerId);
		var disconnectedClient = socketClientManagement.removeDisconnectedClient(playerId);
		disconnectedClient.getAllRooms().forEach(room -> {
			if (!room.equals(DEFAULT_GENERAL_ROOM) && !room.equals("")) {
				var roomId = Integer.parseInt(room);
				var roomDto = roomConverter.toDto(roomRepository.findOneById(roomId));
				server.getRoomOperations(room).sendEvent(SocketEventMessage.MEMBER_LEFT, roomDto);
			}
		});
	}
}