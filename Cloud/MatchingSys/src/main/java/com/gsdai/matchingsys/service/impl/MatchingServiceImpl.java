package com.gsdai.matchingsys.service.impl;

import com.gsdai.matchingsys.service.MatchingService;
import com.gsdai.matchingsys.service.utils.MatchPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchPool matchPool = new MatchPool();

    @Override
    public String addPlayer(Integer userId, Integer marks, Integer botId) {
        System.out.println("add player: " + userId + " " + marks + " " + botId);
        matchPool.addPlayer(userId, marks, botId);
        return "add player success";
    }

    @Override
    public String deletePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        matchPool.deletePlayer(userId);
        return "remove player success";
    }
}
