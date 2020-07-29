package com.mmlab.m1.game.module;

public class Id {

    public Id(int user_id, int group_id, int room_id, int game_id, int chest_id, int src_id) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.room_id = room_id;
        this.game_id = game_id;
        this.chest_id = chest_id;
        this.src_id = src_id;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getChest_id() {
        return chest_id;
    }

    public void setChest_id(int chest_id) {
        this.chest_id = chest_id;
    }

    public int getSrc_id() {
        return src_id;
    }

    public void setSrc_id(int src_id) {
        this.src_id = src_id;
    }

    int user_id;
    int group_id;
    int room_id;
    int game_id;
    int chest_id;
    int src_id;

}
