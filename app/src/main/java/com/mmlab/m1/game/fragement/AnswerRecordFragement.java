package com.mmlab.m1.game.fragement;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mmlab.m1.R;
import com.mmlab.m1.game.ApiService;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.db.DbHandler;
import com.mmlab.m1.game.module.AnswerRecordShowing;
import com.mmlab.m1.game.module.Id;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AnswerRecordFragement extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MapFragement mapFragement;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DbHandler dbHandler;
    private SQLiteDatabase db;

    FloatingActionButton main_fab;

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(GameActivity.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiService api = retrofit.create(ApiService.class);
    Call<List<AnswerRecordShowing>> ansShowCall;

    List<AnswerRecordShowing> dataset;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.answer_record, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.answer_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        apiAnswerShowing(GameActivity.getUser_id(),GameActivity.getGame_id());


        //main fab in main activity
        main_fab = (FloatingActionButton) rootView.findViewById(R.id.main_fab);
        main_fab.setVisibility(View.VISIBLE);
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }
        });



        return rootView;
    }

    public void apiAnswerShowing(final int user_id,final int game_id){
        ansShowCall = api.getAnsShowData(new Id(user_id,0,0,game_id,0,0));
        ansShowCall.enqueue(new Callback<List<AnswerRecordShowing>>() {
            @Override
            public void onResponse(Call<List<AnswerRecordShowing>> call, Response<List<AnswerRecordShowing>> response) {
                Log.d("msg", " get user answer history : " + response);
                if(response.body()!=null){
                    Log.d("msg", " to bulid adapter for answer history ");
                    dataset = response.body();
                    mAdapter = new AnswerRecordAdapter(dataset);
                    mRecyclerView.setAdapter(mAdapter);
                }
                else{
                    Log.d("msg", "user has not answer yet");
                }

            }

            @Override
            public void onFailure(Call<List<AnswerRecordShowing>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
