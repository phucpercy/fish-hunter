package com.percy.fish_hunter.converter;

import com.percy.fish_hunter.dto.PlayerDto;
import com.percy.fish_hunter.models.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerConverter {
	public PlayerDto toDto(Player entity) {
		PlayerDto dto = new PlayerDto();
		dto.setId(entity.getId());
		dto.setUsername(entity.getUsername());
		
		return dto;
	}

	public List<PlayerDto> toListDto(List<Player> players) {
		List<PlayerDto> dtos = new ArrayList<>();

		players.forEach(e -> dtos.add(toDto(e)));

		return dtos;
	}
}
