package com.gsdai.backend.controller.battle;

import com.gsdai.backend.service.battle.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {

    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("/battle/botmove")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveService.receiveBotMove(userId, direction);
    }
}
