package com.mmlab.m1.game.module;

import android.media.MediaPlayer;

import java.io.IOException;

public class UrlMediaPlayer extends MediaPlayer
{
    String dataSource;

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
    {
        // TODO Auto-generated method stub
        super.setDataSource(path);
        dataSource = path;
    }

    public String getDataSource()
    {
        return dataSource;
    }
}