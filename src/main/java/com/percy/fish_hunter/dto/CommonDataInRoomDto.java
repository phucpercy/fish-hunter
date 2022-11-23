package com.percy.fish_hunter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonDataInRoomDto {

    private int playerId;
    private int roomId;
    private JsonNode data;
}
