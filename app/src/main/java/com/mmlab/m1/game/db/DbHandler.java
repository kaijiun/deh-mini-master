package com.mmlab.m1.game.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE user_data " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "username TEXT  NOT NULL , " +
                "user_id INTEGER NOT NULL , " +
                "passward TEXT  NOT NULL )" );
        db.execSQL("CREATE  TABLE group_data" +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "group_name TEXT  NOT NULL , " +
                "group_id INTEGER NOT NULL , " +
                "group_leader_id TEXT  NOT NULL )" );
        db.execSQL("CREATE  TABLE answer_data" +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "chest_id INTEGER  NOT NULL , " +
                "question TEXT  NOT NULL , " +
                "answer INTEGER NOT NULL , " +
                "correctness INTEGER NOT NULL , " +
                "point INTEGER  NOT NULL )" );
        db.execSQL("CREATE  TABLE game_data " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "game_id INTEGER  NOT NULL , " +
                "group_id INTEGER  NOT NULL , " +
                "start_time TEXT NOT NULL ," +
                "end_time TEXT NOT NULL ," +
                "play_time INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists roles");
        onCreate(db);
    }
}
