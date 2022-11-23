package com.percy.fish_hunter.config;

import com.percy.fish_hunter.models.Room;
import com.percy.fish_hunter.models.RoomStatus;
import com.percy.fish_hunter.models.RoomType;
import com.percy.fish_hunter.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class DataLoader implements ApplicationRunner {

	private RoomRepository roomRepository;

	public void run(ApplicationArguments args) throws Exception {

		log.info("================INIT DB=================");

		for (int i = 1; i <= 50; i++) {
			roomRepository.save(new Room(RoomType.TWO, RoomStatus.WAITING));
		}
		for (int i = 1; i <= 50; i++) {
			roomRepository.save(new Room(RoomType.QUAD, RoomStatus.WAITING));
		}
	}
}