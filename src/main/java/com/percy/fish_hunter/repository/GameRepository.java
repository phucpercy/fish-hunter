package com.percy.fish_hunter.repository;

import com.percy.fish_hunter.models.Game;
import com.percy.fish_hunter.models.GameStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {

	List<Game> findAll();

	List<Game> findAll(Sort sort);

	List<Game> findAllByStatus(GameStatus status);

	void
}
