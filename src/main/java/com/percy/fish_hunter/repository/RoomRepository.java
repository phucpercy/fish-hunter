package com.percy.fish_hunter.repository;

import com.percy.fish_hunter.models.Room;
import com.percy.fish_hunter.models.RoomStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findAll(Sort sort);

    Room findOneById(int id);

    List<Room> findAllByStatus(RoomStatus status);
}
