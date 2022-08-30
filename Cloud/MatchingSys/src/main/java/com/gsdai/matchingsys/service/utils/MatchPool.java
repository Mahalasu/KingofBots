package com.gsdai.matchingsys.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchPool extends Thread {

    private static List<Player> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:8081/battle/startgame";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer marks, Integer botId) {
        lock.lock();
        try {
            players.add(new Player(userId, marks, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void deletePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players)
                if (!player.getUserId().equals(userId))
                    newPlayers.add(player);
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void incWaitingTime() {
        for (Player player : players)
            player.setWaitingTime(player.getWaitingTime() + 1);
    }

    private boolean checkMatchingValid(Player a, Player b) {
        int marksDelta = Math.abs(a.getMarks() - b.getMarks());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return marksDelta <= waitingTime * 10;
    }

    private void sendResult(Player a, Player b) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("aBotId", a.getBotId().toString());
        data.add("bId", b.getUserId().toString());
        data.add("bBotId", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    private void matchPlayers() {
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j< players.size(); j++) {
                if (used[j]) continue;
                Player a = players.get(i), b = players.get(j);
                if (checkMatchingValid(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++)
            if (!used[i])
                newPlayers.add(players.get(i));
        players = newPlayers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    incWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
