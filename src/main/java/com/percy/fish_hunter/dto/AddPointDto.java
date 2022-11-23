package com.percy.fish_hunter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPointDto {

    private int playerId;
    private int gameId;
    private int roomId;
    private int point;
}
