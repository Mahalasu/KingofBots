package com.gsdai.backend.utils;

import com.alibaba.fastjson.JSONObject;
import com.gsdai.backend.consumer.WebSocketServer;
import com.gsdai.backend.domain.Bot;
import com.gsdai.backend.domain.Record;
import com.gsdai.backend.domain.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer numOfObstacles;
    private final int[][] map;
    private final static int[] dx = {-1, 0, 1, 0};
    private final static int[] dy = {0, 1, 0, -1};
    private final Player playerA;
    private final Player playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing -> finished
    private String loser = ""; // both: draw; a: a lose; b: b lose
    private final static String addBotUrl = "http://127.0.0.1:8083/bot/add";

    public Game(Integer rows, Integer cols, Integer numOfObstacles, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.numOfObstacles = numOfObstacles;
        this.map = new int[rows][cols];

        Integer aBotId = -1, bBotId = -1;
        String aBotCode = "", bBotCode = "";
        if (botA != null) {
            aBotId = botA.getId();
            aBotCode = botA.getCode();
        }
        if (botB != null) {
            bBotId = botB.getId();
            bBotCode = botB.getCode();
        }

        this.playerA = new Player(idA, aBotId, aBotCode, this.rows - 1, 0, new ArrayList<>());
        this.playerB = new Player(idB, bBotId, bBotCode, 0, this.cols - 1, new ArrayList<>());
    }

    public Player getPlayerA() {
        return this.playerA;
    }

    public Player getPlayerB() {
        return this.playerB;
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        this.map[sx][sy] = 1;

        for (int i = 0; i < dx.length; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x < 0 || y < 0 || x >= this.rows || y >= this.cols)
                continue;
            if (this.map[x][y] == 0 && this.checkConnectivity(x, y, tx, ty)) {
                this.map[sx][sy] = 0;
                return true;
            }
        }

        this.map[sx][sy] = 0;
        return false;
    }

    public int[][] getMap() {
        return this.map;
    }

    private boolean draw() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.map[i][j] = 0;
            }
        }

        Random random = new Random();
        for (int i = 0; i < this.numOfObstacles / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (this.map[r][c] == 1 || this.map[this.rows - 1 - r][this.cols - 1 - c] == 1
                        || r == this.rows - 1 && c == 0 || r == 0 && c == this.cols - 1)
                    continue;
                this.map[r][c] = this.map[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return checkConnectivity(this.rows - 1, 0, 0, this.cols - 1);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

    public void setNextStepA(Integer nextStepA) {
        this.lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            this.lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        this.lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            this.lock.unlock();
        }
    }

    private String getInput(Player player) {
        Player self, opponent;
        if (playerA.getId().equals(player.getId())) {
            self = playerA;
            opponent = playerB;
        } else {
            self = playerB;
            opponent = playerA;
        }

        return getMapString() + "#"
                + self.getSx() + "#"
                + self.getSy() + "#("
                + self.getStepsString() + ")#"
                + opponent.getSx() + "#"
                + opponent.getSy() + "#("
                + opponent.getStepsString() + ")";
    }

    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", player.getId().toString());
        data.add("code", player.getCode());
        data.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                this.lock.lock();
                try {
                    if (this.nextStepA != null && this.nextStepB != null) {
                        this.playerA.getSteps().add(this.nextStepA);
                        this.playerB.getSteps().add(this.nextStepB);
                        return true;
                    }
                } finally {
                    this.lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (cell.getX() < 0 || cell.getX() >= this.rows || cell.getY() < 0 || cell.getY() >= this.cols || this.map[cell.getX()][cell.getY()] == 1)
            return false;

        for (int i = 0; i < n - 1; i++) {
            Cell tempCell = cellsA.get(i);
            if (tempCell.getX() == cell.getX() && tempCell.getY() == cell.getY())
                return false;
        }

        for (int i = 0; i < n - 1; i++) {
            Cell tempCell = cellsB.get(i);
            if (tempCell.getX() == cell.getX() && tempCell.getY() == cell.getY())
                return false;
        }

        return true;
    }

    private void judge() {
        List<Cell> cellsA = this.playerA.getCells();
        List<Cell> cellsB = this.playerB.getCells();
        boolean validA = this.checkValid(cellsA, cellsB);
        boolean validB = this.checkValid(cellsB, cellsA);
        if (!validA || !validB) {
            this.status = "finished";
            if (!validA && !validB)
                this.loser = "both";
            else if (!validA) {
                this.loser = "a";
            } else {
                this.loser = "b";
            }
        }
    }

    private void sendResp(String resp) {
        if (WebSocketServer.users.get(this.playerA.getId()) != null)
            WebSocketServer.users.get(this.playerA.getId()).sendMessage(resp);
        if (WebSocketServer.users.get(this.playerB.getId()) != null)
            WebSocketServer.users.get(this.playerB.getId()).sendMessage(resp);
    }

    private void sendSteps() {
        this.lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "steps");
            resp.put("aSteps", this.nextStepA);
            resp.put("bSteps", this.nextStepB);
            this.sendResp(resp.toJSONString());
            this.nextStepA = this.nextStepB = null;
        } finally {
            this.lock.unlock();
        }
    }

    private String getMapString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.rows; i++)
            for (int j = 0; j < this.cols; j++)
                result.append(this.map[i][j]);

        return result.toString();
    }

    private void updateMarks(Player player, Integer marks) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setMarks(marks);
        WebSocketServer.userMapper.updateById(user);
    }

    private void saveToDatabase() {
        Integer marksA = WebSocketServer.userMapper.selectById(playerA.getId()).getMarks();
        Integer marksB = WebSocketServer.userMapper.selectById(playerB.getId()).getMarks();
        if ("a".equals(loser)) {
            marksA -= 2;
            marksB += 5;
        } else if ("b".equals(loser)) {
            marksA += 5;
            marksB -= 2;
        }

        updateMarks(playerA, marksA);
        updateMarks(playerB, marksB);

        Record record = new Record(
                null,
                this.playerA.getId(),
                this.playerA.getSx(),
                this.playerA.getSy(),
                this.playerB.getId(),
                this.playerB.getSx(),
                this.playerB.getSy(),
                this.playerA.getStepsString(),
                this.playerB.getStepsString(),
                this.getMapString(),
                this.loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", this.loser);
        saveToDatabase();
        this.sendResp(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (this.nextStep()) {
                judge();
                if (this.status.equals("playing")) {
                    this.sendSteps();
                } else {
                    this.sendResult();
                    break;
                }
            } else {
                this.status = "finished";
                this.lock.lock();
                try {
                    if (this.nextStepA == null && this.nextStepB == null)
                        this.loser = "both";
                    else if (this.nextStepA == null)
                        this.loser = "a";
                    else
                        this.loser = "b";
                } finally {
                    this.lock.unlock();
                }
                this.sendResult();
                break;
            }
        }
    }
}
