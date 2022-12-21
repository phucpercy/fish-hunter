package com.percy.fish_hunter.listenner;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.percy.fish_hunter.repository.RoomMemberRepository;
import com.percy.fish_hunter.service.GameService;
import com.percy.fish_hunter.support.SocketClientManagement;
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

	private final SocketClientManagement socketClientManagement;
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
	}
}