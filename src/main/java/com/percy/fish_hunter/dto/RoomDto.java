package com.percy.fish_hunter.dto;

import com.percy.fish_hunter.models.RoomStatus;
import com.percy.fish_hunter.models.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

	private int id;
	private RoomType roomType;
	private RoomStatus status;
	private Date createdDate;
	private List<PlayerDto> roomMembers;
}
