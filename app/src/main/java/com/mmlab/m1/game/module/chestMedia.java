package com.mmlab.m1.game.module;

public class chestMedia {
    int ATT_id;
    String ATT_url;
    String ATT_format;

    public int getATT_id() {
        return ATT_id;
    }

    public void setATT_id(int ATT_id) {
        this.ATT_id = ATT_id;
    }

    public String getATT_url() {
        return ATT_url;
    }

    public void setATT_url(String ATT_url) {
        this.ATT_url = ATT_url;
    }

    public String getATT_format() {
        return ATT_format;
    }

    public void setATT_format(String ATT_Format) {
        this.ATT_format = ATT_Format;
    }

    public chestMedia(int ATT_id, String ATT_url, String ATT_Format) {
        this.ATT_id = ATT_id;
        this.ATT_url = ATT_url;
        this.ATT_format = ATT_Format;
    }


}
