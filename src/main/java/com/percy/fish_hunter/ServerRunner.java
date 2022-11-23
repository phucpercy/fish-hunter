package com.percy.fish_hunter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServerRunner implements CommandLineRunner {
	@Autowired
	private SocketIOServer server;

	@Override
	public void run(String... args) throws Exception {
		server.start();
		log.info("Socket launch successful!");
	}
}