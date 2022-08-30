package com.gsdai.backend.service.impl.utils;

import com.gsdai.backend.consumer.WebSocketServer;
import com.gsdai.backend.service.battle.ReceiveBotMoveService;
import com.gsdai.backend.utils.Game;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {

    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                }
                else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }
}
