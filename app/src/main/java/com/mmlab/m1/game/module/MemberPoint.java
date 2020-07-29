package com.mmlab.m1.game.module;

public class MemberPoint {

    boolean correctness;
    int user_id_id;
    int point;
    String nickname;
    String answer_time;

    public MemberPoint(boolean correctness, int user_id_id, int point, String nickname, String answer_time) {
        this.correctness = correctness;
        this.user_id_id = user_id_id;
        this.point = point;
        this.nickname = nickname;
        this.answer_time = answer_time;
    }

    public boolean isCorrectness() {
        return correctness;
    }

    public void setCorrectness(boolean correctness) {
        this.correctness = correctness;
    }

    public int getUser_id_id() {
        return user_id_id;
    }

    public void setUser_id_id(int user_id_id) {
        this.user_id_id = user_id_id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }


}
