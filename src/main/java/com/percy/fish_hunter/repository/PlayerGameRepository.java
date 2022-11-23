package com.percy.fish_hunter.repository;

import com.percy.fish_hunter.models.PlayerGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, Integer> {

    PlayerGame findOneByPrimaryKeyGameIdAndPrimaryKeyPlayerIdAndFinishDateIsNull(int gameId, int playerId);
}
