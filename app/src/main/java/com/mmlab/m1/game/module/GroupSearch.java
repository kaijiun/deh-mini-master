package com.mmlab.m1.game.module;

public class GroupSearch {


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCoi() {
        return coi;
    }

    public void setCoi(String coi) {
        this.coi = coi;
    }

    public GroupSearch(int user_id, String coi) {
        this.user_id = user_id;
        this.coi = coi;
    }

    int user_id;
    String coi;

}
