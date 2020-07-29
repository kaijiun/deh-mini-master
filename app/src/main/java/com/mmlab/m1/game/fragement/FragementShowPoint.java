package com.mmlab.m1.game.fragement;

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
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.module.HistoryGame;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.MemberPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mmlab.m1.game.GameActivity.getApi;

public class FragementShowPoint extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private RecyclerView.Adapter mAdapter;
    private TextView no_result;

    Call<List<MemberPoint>> callapi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_show_point, container, false);



        //build recycle view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_show_point);
        mLayoutManager  = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        no_result = (TextView) rootView.findViewById(R.id.no_result);

        callApiToShow();


        return rootView;
    }
    public boolean inThisArray(List<String> temp , String s){
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).equals(s)){
                return true;
            }
        }

        return false;
    }

    public void callApiToShow(){
        callapi = getApi().getMemberPoint(new Id(0,0, 0,GameActivity.getGame_id(),0,0));
        callapi.enqueue(new Callback<List<MemberPoint>>() {
            @Override
            public void onResponse(Call<List<MemberPoint>> call2, Response<List<MemberPoint>> response) {
                Log.d("OAO", "get MemberPoint list sucess");
                if(response.body()!=null) {
                    Log.d("OAO", "MemberPoint list size : " + response.body().size());

                    List<MemberPoint> newlist = new ArrayList<MemberPoint>();
                    List<String> temp = new ArrayList<String>();

                    Collections.reverse(response.body());

                    for(int i = 0; i<response.body().size(); i++){
                        MemberPoint s = response.body().get(i);
                        if(inThisArray(temp,s.getNickname())){
                            for(int j = 0; j< newlist.size(); j++){
                                if(newlist.get(j).getNickname().equals(s.getNickname())){
                                    if(s.isCorrectness()){
                                        newlist.get(j).setPoint(newlist.get(j).getPoint() + s.getPoint());
                                    }
                                    else{
                                        newlist.get(j).setPoint(newlist.get(j).getPoint() + 0);
                                    }
                                    break;
                                }
                            }
                        }
                        else{
                            if(s.isCorrectness()){
                                newlist.add(new MemberPoint(true,s.getUser_id_id(),s.getPoint(),s.getNickname(), s.getAnswer_time()));
                                temp.add(s.getNickname());
                            }
                            else{
                                newlist.add(new MemberPoint(true,s.getUser_id_id(),0,s.getNickname(), s.getAnswer_time()));
                                temp.add(s.getNickname());
                            }

                        }

                    }

                    Log.d("OAO", "reMemberPoint list size : " + newlist.size());

                    Collections.sort(newlist, new Comparator<MemberPoint>() {
                        @Override
                        public int compare(MemberPoint o1, MemberPoint o2) {
                            Integer obj1 = new Integer(o1.getPoint());
                            Integer obj2 = new Integer(o2.getPoint());
                            return obj1.compareTo(obj2);
                        }
                    });

                    Collections.reverse(newlist);

                    mAdapter = new AdapterShowPoint(newlist,getActivity());
                    mRecyclerView.setAdapter(mAdapter);

                }
                else{
                    Log.d("OAO", "MemberPoint list size is null");
                    no_result.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<MemberPoint>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


}
