package com.percy.fish_hunter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayResponse {

    private long timeLeft;
    private Collection<FishAssetResponse> fishAssets;
    private RoomDto room;
}
