package com.mmlab.m1.game.module;

public class LoginForm {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCoiname() {
        return coiname;
    }

    public void setCoiname(String coiname) {
        this.coiname = coiname;
    }

    String username;
    String password;
    String coiname;

    public LoginForm(String un, String pw, String coi){
        username = un;
        password = pw;
        coiname = coi;
    }

}
