package com.gsdai.backend.service.impl.battle;

import com.gsdai.backend.consumer.WebSocketServer;
import com.gsdai.backend.service.battle.GameStartService;
import org.springframework.stereotype.Service;

@Service
public class GameStartServiceImpl implements GameStartService {

    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        System.out.println("start game: " + aId + " " + aBotId + " " + bId + " " + bBotId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}
