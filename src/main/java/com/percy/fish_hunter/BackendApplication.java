package com.percy.fish_hunter;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class BackendApplication {
	private String host = "127.0.0.1";

	@Bean
	public SocketIOServer socketioserver() {

		var port = 8082;
		try {
			InetAddress ip=InetAddress.getLocalHost();
			this.host = ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);

		// This can be used for authentication
		config.setAuthorizationListener(new AuthorizationListener() {

			@Override
			public boolean isAuthorized(HandshakeData data) {
				return true;
			}
		});

		return new SocketIOServer(config);
	}

	@Bean
	public SpringAnnotationScanner springannotationscanner(SocketIOServer socketserver) {
		return new SpringAnnotationScanner(socketserver);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
