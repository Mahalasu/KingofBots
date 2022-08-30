package com.gsdai.backend.controller.battle;

import com.gsdai.backend.service.battle.GameStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GameStartController {

    @Autowired
    private GameStartService gameStartService;

    @PostMapping("/battle/startgame")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aBotId")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bBotId")));
        return gameStartService.startGame(aId, aBotId, bId, bBotId);
    }
}
