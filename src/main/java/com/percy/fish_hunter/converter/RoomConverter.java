package com.percy.fish_hunter.converter;

import com.percy.fish_hunter.dto.RoomDto;
import com.percy.fish_hunter.models.Room;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RoomConverter {

	private RoomMemberConverter roomMemberConverter;
	private PlayerGameConverter playerGameConverter;

	public RoomDto toDto(Room entity) {
		RoomDto dto = new RoomDto();

		dto.setId(entity.getId());
		dto.setRoomType(entity.getRoomType());
		dto.setStatus(entity.getStatus());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setRoomMembers(roomMemberConverter.toListDto(entity.getRoomMembers()));
		return dto;
	}

	public List<RoomDto> toListDto(List<Room> rooms) {
		List<RoomDto> result = new ArrayList<>();
		rooms.forEach(e -> result.add(toDto(e)));
		return result;
	}
}
