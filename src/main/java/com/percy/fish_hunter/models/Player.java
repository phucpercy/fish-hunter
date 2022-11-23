package com.percy.fish_hunter.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player implements UserDetails {

	@Id
	@GeneratedValue
	private int id;
	private int balance;
	private String avatar;
	private String username;
	private String password;
	private int fee;

	public Player(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object obj) {

		Player player = (Player) obj;

		return Objects.equals(getId(), player.getId());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
