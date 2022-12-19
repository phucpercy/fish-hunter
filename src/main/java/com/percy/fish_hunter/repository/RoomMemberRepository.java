package com.percy.fish_hunter.repository;

import com.percy.fish_hunter.models.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Integer> {

    RoomMember findOneByPrimaryKeyPlayerIdOrderByCreatedDateDesc(int playerId);

    void deleteRoomMemberByPrimaryKeyRoomIdAndPrimaryKeyPlayerId(int roomId, int playerId);

    void deleteRoomMemberByPrimaryKeyPlayerId(int playerId);
}
