package com.gsdai.matchingsys.service.utils;

public class Player {

    private Integer userId;
    private Integer marks;
    private Integer botId;
    private Integer waitingTime;

    public Player() {
    }

    public Player(Integer userId, Integer marks, Integer botId, Integer waitingTime) {
        this.userId = userId;
        this.marks = marks;
        this.botId = botId;
        this.waitingTime = waitingTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Integer getBotId() {
        return botId;
    }

    public void setBotId(Integer botId) {
        this.botId = botId;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }
}
