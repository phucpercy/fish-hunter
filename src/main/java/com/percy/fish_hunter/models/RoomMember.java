package com.percy.fish_hunter.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AssociationOverrides({
		@AssociationOverride(name = "primaryKey.player", joinColumns = @JoinColumn(name = "player_id")),
		@AssociationOverride(name = "primaryKey.room", joinColumns = @JoinColumn(name = "room_id"))
})
@EntityListeners({AuditingEntityListener.class})
public class RoomMember {
	@EmbeddedId
	private RoomMemberId primaryKey = new RoomMemberId();
	private boolean isDisconnected;
	@Column(updatable = false)
	@CreatedDate
	private Date createdDate;

	@Transient
	public Player getPlayer() {
		return getPrimaryKey().getPlayer();
	}

	public void setPlayer(Player player) {
		getPrimaryKey().setPlayer(player);
	}

	@Transient
	public Room getRoom() {
		return primaryKey.getRoom();
	}

	public void setRoom(Room room) {
		getPrimaryKey().setRoom(room);
	}
}
