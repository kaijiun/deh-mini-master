package com.mmlab.m1.game.callback;

import android.widget.Button;

public interface RoomAdapterCallback {
    void onRoomHasNoGame(Button button, int group_id, String group_name);
    void onRoomIsGaming(Button button, int group_id, String group_name);
    void onRoomUnstartGame(Button button, int group_id, String group_name);

    void onStartRoomGame(Button button, int group_id, String group_name);
    void onGameDataReady();
}
