package com.percy.fish_hunter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.percy.fish_hunter.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @Test
    void testGenerateFish() throws JsonProcessingException {
        var res = gameService.generateFishAsset();
        log.info("random fish asset {}", new ObjectMapper().writeValueAsString(res));
    }

}
