package com.percy.fish_hunter.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerGameDto {

    private PlayerDto player;
    private int point;
    private Date finishDate;
}
