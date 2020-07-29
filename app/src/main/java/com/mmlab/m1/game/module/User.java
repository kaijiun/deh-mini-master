package com.mmlab.m1.game.module;


public class User {
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }



    public User(int user_id, String password, String nickname, String email, String role, String birthday) {
        this.user_id = user_id;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.birthday = birthday;
    }

    public User(){

    }

    private int user_id;
    private String password;
    private String nickname;
    private String email;
    private String role;
    private String birthday;


}
