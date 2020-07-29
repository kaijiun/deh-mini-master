package com.mmlab.m1.game.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.GameHistoryCallback;
import com.mmlab.m1.game.module.HistoryGame;
import com.mmlab.m1.game.module.Id;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mmlab.m1.game.GameActivity.getApi;

public class FragementGameHistory extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private RecyclerView.Adapter mAdapter;

    Call<List<HistoryGame>> callapi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_game_history, container, false);

        //build recycle view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_game_history);
        mLayoutManager  = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        fragmentManager = getActivity().getSupportFragmentManager();

        callApiToShow();



        return rootView;
    }


    public void callApiToShow(){
        callapi = getApi().getGameHistory(new Id(0,0, GameActivity.getRoom_id(),0,0,0));
        callapi.enqueue(new Callback<List<HistoryGame>>() {
            @Override
            public void onResponse(Call<List<HistoryGame>> call2, Response<List<HistoryGame>> response) {
                Log.d("OAO", "get HistoryGame list sucess");
                if(response.body()!=null) {
                    Log.d("OAO", "HistoryGame list size : " + response.body().size());

                    Collections.reverse(response.body());

                    mAdapter = new AdapterGameHistory(response.body(),fragmentManager);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<HistoryGame>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
