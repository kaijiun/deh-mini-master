package com.mmlab.m1.game.module;

public class PickItem {
    String uri;

    public PickItem(String uri, String type) {
        this.uri = uri;
        this.type = type;
    }

    String type;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
