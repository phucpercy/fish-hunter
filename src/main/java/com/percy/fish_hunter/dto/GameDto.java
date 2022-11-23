package com.percy.fish_hunter.dto;

import com.percy.fish_hunter.models.GameStatus;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private int id;
    private GameStatus status;
    private Date createdDate;
    private List<PlayerGameDto> players;
}
