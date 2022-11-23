package com.percy.fish_hunter.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AssociationOverrides({@AssociationOverride(name = "primaryKey.player", joinColumns = @JoinColumn(name = "player_id")),
		@AssociationOverride(name = "primaryKey.game", joinColumns = @JoinColumn(name = "game_id"))})
@EntityListeners({ AuditingEntityListener.class })
public class PlayerGame {
	@EmbeddedId
	private PlayerGameId primaryKey = new PlayerGameId();
	private int point;
	private Date finishDate;
	@Column(updatable = false)
	@CreatedDate
	private Date createdDate;

	@Override
	public boolean equals(Object obj) {

		PlayerGame playerGame = (PlayerGame) obj;

		return Objects.equals(getPlayer().getId(), playerGame.getPlayer().getId());
	}

	@Transient
	public Player getPlayer() {
		return getPrimaryKey().getPlayer();
	}

	public void setPlayer(Player player) {
		getPrimaryKey().setPlayer(player);
	}

	@Transient
	public Game getGame() {
		return primaryKey.getGame();
	}

	public void setGame(Game game) {
		getPrimaryKey().setGame(game);
	}

}
