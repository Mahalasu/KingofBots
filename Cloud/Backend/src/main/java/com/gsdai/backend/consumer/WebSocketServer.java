package com.gsdai.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.BotMapper;
import com.gsdai.backend.mapper.RecordMapper;
import com.gsdai.backend.mapper.UserMapper;
import com.gsdai.backend.utils.Game;
import com.gsdai.backend.utils.JwtAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private User user;
    private Session session = null;
    public static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static BotMapper botMapper;
    public static RestTemplate restTemplate;
    public Game game = null;
    private final static String addPlayerUrl = "http://127.0.0.1:8082/player/add";
    private final static String deletePlayerUrl = "http://127.0.0.1:8082/player/delete";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapperMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("connected");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (user != null)
            users.put(userId, this);
        else
            this.session.close();
    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer playerAId, Integer aBotId, Integer playerBId, Integer bBotId) {
        User playerA = userMapper.selectById(playerAId), playerB = userMapper.selectById(playerBId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        Game game = new Game(13, 14, 30,
                playerA.getId(), botA,
                playerB.getId(), botB);
        game.createMap();
        if (users.get(playerA.getId()) != null)
            users.get(playerA.getId()).game = game;
        if (users.get(playerB.getId()) != null)
            users.get(playerB.getId()).game = game;
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("playerAId", game.getPlayerA().getId());
        respGame.put("playerBId", game.getPlayerB().getId());
        respGame.put("playerAsx", game.getPlayerA().getSx());
        respGame.put("playerAsy", game.getPlayerA().getSy());
        respGame.put("playerBsx", game.getPlayerB().getSx());
        respGame.put("playerBsy", game.getPlayerB().getSy());
        respGame.put("gameMap", game.getMap());

        JSONObject respFirst = new JSONObject(), respSecond = new JSONObject();
        respFirst.put("event", "opponentFound");
        respFirst.put("opponentUsername", playerB.getUsername());
        respFirst.put("opponentAvatar", playerB.getAvatar());
        respFirst.put("game", respGame);
        if (users.get(playerA.getId()) != null)
            users.get(playerA.getId()).sendMessage(respFirst.toJSONString());

        respSecond.put("event", "opponentFound");
        respSecond.put("opponentUsername", playerA.getUsername());
        respSecond.put("opponentAvatar", playerA.getAvatar());
        respSecond.put("game", respGame);
        if (users.get(playerB.getId()) != null)
            users.get(playerB.getId()).sendMessage(respSecond.toJSONString());
    }

    private void startMatching(Integer botId) {
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", this.user.getId().toString());
        data.add("marks", this.user.getMarks().toString());
        data.add("botId", botId.toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void cancelMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", this.user.getId().toString());
        restTemplate.postForObject(deletePlayerUrl, data, String.class);
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) {
                game.setNextStepA(direction);
            }
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("startMatching".equals(event))
            this.startMatching(data.getInteger("botId"));
        else if ("cancelMatching".equals(event))
            this.cancelMatching();
        else if ("keyInputMove".equals(event))
            this.move(data.getInteger("direction"));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
