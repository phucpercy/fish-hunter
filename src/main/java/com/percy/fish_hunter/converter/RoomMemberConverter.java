package com.percy.fish_hunter.converter;

import com.percy.fish_hunter.dto.PlayerDto;
import com.percy.fish_hunter.models.RoomMember;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class RoomMemberConverter {
    public PlayerDto toDto(RoomMember entity) {
        PlayerDto dto = new PlayerDto();
        dto.setId(entity.getPlayer().getId());
        dto.setUsername(entity.getPlayer().getUsername());

        return dto;
    }

    public List<PlayerDto> toListDto(List<RoomMember> roomMembers) {
        List<PlayerDto> dtos = new ArrayList<>();

        roomMembers.forEach(e -> dtos.add(toDto(e)));

        return dtos;
    }
}
