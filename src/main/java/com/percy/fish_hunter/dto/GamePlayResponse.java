package com.percy.fish_hunter.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayResponse {

    private long timeLeft;
    private Collection<FishAssetResponse> fishAssets;
}
