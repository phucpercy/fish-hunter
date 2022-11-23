package com.percy.fish_hunter.converter;

import com.percy.fish_hunter.dto.PlayerGameDto;
import com.percy.fish_hunter.models.PlayerGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerGameConverter {

	@Autowired
	private PlayerConverter playerConverter;

	public PlayerGameDto toDto(PlayerGame entity) {
		PlayerGameDto dto = new PlayerGameDto();
		dto.setPlayer(playerConverter.toDto(entity.getPlayer()));
		dto.setPoint(entity.getPoint());
		dto.setFinishDate(entity.getFinishDate());
		return dto;
	}

	public List<PlayerGameDto> toListDto(List<PlayerGame> entities) {
		List<PlayerGameDto> dtos = new ArrayList<>();
		entities.forEach(e -> dtos.add(toDto(e)));

		return dtos;
	}
}
