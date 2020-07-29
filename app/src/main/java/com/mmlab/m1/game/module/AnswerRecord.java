package com.mmlab.m1.game.module;

public class AnswerRecord {

    public AnswerRecord(int user_id, String answer, int correctness, int chest_id, int game_id, double lat, double lng, int point) {
        this.user_id = user_id;
        this.answer = answer;
        this.correctness = correctness;
        this.chest_id = chest_id;
        this.game_id = game_id;
        this.lat = lat;
        this.lng = lng;
        this.point = point;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCorrectness() {
        return correctness;
    }

    public void setCorrectness(int correctness) {
        this.correctness = correctness;
    }

    public int getChest_id() {
        return chest_id;
    }

    public void setChest_id(int chest_id) {
        this.chest_id = chest_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    int user_id;
    String answer;
    int correctness;
    int chest_id;
    int game_id;
    double lat;
    double lng;
    int point;
}
