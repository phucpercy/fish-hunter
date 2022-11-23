package com.percy.fish_hunter.support;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SocketEventMessage {

    public static final String TIME_LEFT = "time_left";
    public static final String GAME_RESULT = "game_result";
    public static final String ROOM_MEMBERS_CHANGED = "room_members_changed";
    public static final String START_GAME = "start_game";
    public static final String JOIN_GAME = "join_game";
    public static final String COMMON_DATA = "common_data";
    public static final String MEMBER_LEFT = "member_left";
    public static final String POINT_CHANGED = "point_changed";
}
