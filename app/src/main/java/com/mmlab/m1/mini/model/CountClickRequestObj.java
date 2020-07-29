package com.mmlab.m1.mini.model;

public class CountClickRequestObj {
    public int getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(int poi_id) {
        this.poi_id = poi_id;
    }

    public CountClickRequestObj(int poi_id) {
        this.poi_id = poi_id;
    }

    int poi_id;

}
