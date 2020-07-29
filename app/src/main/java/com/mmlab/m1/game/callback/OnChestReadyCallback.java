package com.mmlab.m1.game.callback;



import com.mmlab.m1.game.module.Chest;

import java.util.List;

public interface OnChestReadyCallback {
    void onChestDataComplete(List<Chest> dataChestList);
    void onNoChestData();
}
