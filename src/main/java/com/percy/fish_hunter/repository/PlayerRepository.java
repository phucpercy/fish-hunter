package com.percy.fish_hunter.repository;

import com.percy.fish_hunter.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

	Player findOneByUsername(String username);

	Player findOneById(int id);
}
