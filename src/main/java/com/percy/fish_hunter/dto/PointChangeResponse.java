package com.percy.fish_hunter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointChangeResponse {
    private int playerId;
    private int point;
    private UUID fishId;
}
