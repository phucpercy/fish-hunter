package com.percy.fish_hunter.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FishAssetResponse {

    private UUID id;
    private int x;
    private int y;
    private double vx;
    private double vy;
    private double angle;
    private int level;
    private int maxWidth;
    private int maxHeight;
}
