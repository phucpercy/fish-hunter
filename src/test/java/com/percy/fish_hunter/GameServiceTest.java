package com.percy.fish_hunter;

import com.percy.fish_hunter.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @Test
    void testGenerateFish() {
        var res = gameService.generateFishAsset();
        System.out.println(res);
    }

}
