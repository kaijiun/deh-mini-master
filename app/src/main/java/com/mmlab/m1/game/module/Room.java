package com.mmlab.m1.game.module;

public class Room {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getIs_playing() {
        return is_playing;
    }

    public void setIs_playing(int is_playing) {
        this.is_playing = is_playing;
    }

    int id;

    public Room(int id, String room_name, int is_playing) {
        this.id = id;
        this.room_name = room_name;
        this.is_playing = is_playing;
    }

    String room_name;
    int is_playing;
}
