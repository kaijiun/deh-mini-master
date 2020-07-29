package com.mmlab.m1.game.module;

public class HistoryGame {
    public HistoryGame(int id, String start_time) {
        this.id = id;
        this.start_time = start_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    int id;
    String start_time;
}
