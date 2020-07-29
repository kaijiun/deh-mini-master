package com.mmlab.m1.game.module;

public class Group {

    public Group(String group_name, int group_id, int group_leader_id) {
        this.group_name = group_name;
        this.group_id = group_id;
        this.group_leader_id = group_leader_id;
    }

    String group_name;
    int group_id;
    int group_leader_id;

    public int getGroup_leader_id() {
        return group_leader_id;
    }

    public void setGroup_leader_id(int group_leader_id) {
        this.group_leader_id = group_leader_id;
    }



    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }



}
