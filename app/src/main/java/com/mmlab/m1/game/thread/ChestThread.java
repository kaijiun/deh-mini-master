package com.mmlab.m1.game.thread;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mmlab.m1.game.ApiService;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.OnChestReadyCallback;
import com.mmlab.m1.game.module.Chest;
import com.mmlab.m1.game.module.Id;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChestThread {

    private OnChestReadyCallback onChestReadyCallback;
    private List<Chest> chests = new ArrayList<Chest>();
    private LatLng latLng;
    static boolean doRun = true;


    public static boolean isDoRun() {
        return doRun;
    }

    public static void setDoRun(boolean tdoRun) {
        doRun = tdoRun;
    }


    public ChestThread(OnChestReadyCallback onChestReadyCallback){
        this.onChestReadyCallback = onChestReadyCallback;
    }

    public void startMarkUpdateThread(final int game_id,final int user_id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(doRun){
                    try {
                        //get data

                        Retrofit retrofit = new Retrofit.Builder().baseUrl(GameActivity.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                        ApiService api = retrofit.create(ApiService.class);
                        Call<List<Chest>> call2 = api.getTreasure(new Id(user_id,0,0,game_id,0,0));
                        call2.enqueue(new Callback<List<Chest>>() {
                            @Override
                            public void onResponse(Call<List<Chest>> call2, Response<List<Chest>> response) {
                                Log.d("msg", "get chest list : "+response.body());
                                chests = response.body();
                                if(chests!=null) {
                                    Log.d("msg", "chest list size : "+response.body().size());
//                                    for (int i = 0; i < chests.size(); i++) {
//                                        Chest temp = chests.get(i);
//                                        onChestReadyCallback.onChestDataComplete(i, temp);
//                                    }
                                    onChestReadyCallback.onChestDataComplete(response.body());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Chest>> call, Throwable t) {
                                Log.d("Error01", t.getMessage());
                            }
                        });

                        Thread.sleep(20000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }


}
