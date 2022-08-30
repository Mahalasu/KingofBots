package com.gsdai.backend.utils;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Integer id;
    private Integer botId;
    private String code;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    public Player() {
    }

    public Player(Integer id, Integer botId, String code, Integer sx, Integer sy, List<Integer> steps) {
        this.id = id;
        this.botId = botId;
        this.code = code;
        this.sx = sx;
        this.sy = sy;
        this.steps = steps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBotId() {
        return botId;
    }

    public void setBotId(Integer botId) {
        this.botId = botId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSx() {
        return sx;
    }

    public void setSx(Integer sx) {
        this.sx = sx;
    }

    public Integer getSy() {
        return sy;
    }

    public void setSy(Integer sy) {
        this.sy = sy;
    }

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }

    public boolean checkTailInc(int step) {
        return step <= 10 || step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        int round = 0;
        int x = sx, y = sy;
        cells.add(new Cell(x, y));
        for (int step : steps) {
            x += dx[step];
            y += dy[step];
            cells.add(new Cell(x, y));
            if (!this.checkTailInc(++round))
                cells.remove(0);
        }

        return cells;
    }

    public String getStepsString() {
        StringBuilder result = new StringBuilder();
        for (int d : this.steps) {
            result.append(d);
        }
        return result.toString();
    }
}
