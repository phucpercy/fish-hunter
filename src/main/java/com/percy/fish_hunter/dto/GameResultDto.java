package com.percy.fish_hunter.dto;

import lombok.Data;

@Data
public class GameResultDto {

    private boolean isDraw;
    private PlayerDto winner;
}
