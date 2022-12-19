package com.percy.fish_hunter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPointDto {

    private int playerId;
    private int gameId;
    private int roomId;
    private UUID fishId;
}
