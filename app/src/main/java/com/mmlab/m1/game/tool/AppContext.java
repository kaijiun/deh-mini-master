package com.mmlab.m1.game.tool;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    private static Context mContext;

    public void onCreate(){
        mContext = this.getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }

}
