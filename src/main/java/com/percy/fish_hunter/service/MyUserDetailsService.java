package com.percy.fish_hunter.service;

import com.percy.fish_hunter.models.Player;
import com.percy.fish_hunter.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Player loadUserByUsername(String username) throws UsernameNotFoundException {

		return playerRepository.findOneByUsername(username);
	}

	public Player createNewPlayer(String username) {
		var newPlayer = new Player(username);

		playerRepository.save(newPlayer);

		return newPlayer;
	}

}
