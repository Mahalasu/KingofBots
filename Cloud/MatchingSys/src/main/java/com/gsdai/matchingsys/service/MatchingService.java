package com.gsdai.matchingsys.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer marks, Integer botId);
    String deletePlayer(Integer userId);
}
