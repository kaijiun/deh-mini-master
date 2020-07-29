package com.mmlab.m1.game.module;

public class GameData {


    public GameData(String start_time, int end_time, int play_time, int id, int group_id_id) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.play_time = play_time;
        this.id = id;
        this.group_id_id = group_id_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public int getPlay_time() {
        return play_time;
    }

    public void setPlay_time(int play_time) {
        this.play_time = play_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id_id() {
        return group_id_id;
    }

    public void setGroup_id_id(int group_id_id) {
        this.group_id_id = group_id_id;
    }

    String start_time;
    int end_time;
    int play_time;
    int id;
    int group_id_id;
}
