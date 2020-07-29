package com.mmlab.m1.game.module;

public class AnswerSending {
    int chest_id;
    String user_answer;
    int user_id;
    int game_id;

    public AnswerSending(int chest_id, String user_answer, int user_id, int game_id) {
        this.chest_id = chest_id;
        this.user_answer = user_answer;
        this.user_id = user_id;
        this.game_id = game_id;
    }



    public int getChest_id() {
        return chest_id;
    }

    public void setChest_id(int chest_id) {
        this.chest_id = chest_id;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }







}
