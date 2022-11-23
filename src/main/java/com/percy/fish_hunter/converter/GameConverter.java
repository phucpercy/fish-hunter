package com.percy.fish_hunter.converter;

import com.percy.fish_hunter.dto.GameDto;
import com.percy.fish_hunter.models.Game;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GameConverter {

	private PlayerGameConverter playerGameConverter;

	public GameDto toDto(Game entity) {
		GameDto dto = new GameDto();

		dto.setId(entity.getId());
		dto.setStatus(entity.getStatus());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setPlayers(playerGameConverter.toListDto(entity.getPlayerGames()));
		return dto;
	}

	public List<GameDto> toListDto(List<Game> games) {
		List<GameDto> result = new ArrayList<>();
		games.forEach(e -> result.add(toDto(e)));
		return result;
	}
}
