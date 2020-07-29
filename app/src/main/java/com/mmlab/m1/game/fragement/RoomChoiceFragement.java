package com.mmlab.m1.game.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.RoomFragmentCallback;
import com.mmlab.m1.game.thread.ChestThread;


public class RoomChoiceFragement extends Fragment implements RoomFragmentCallback
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentManager fragmentManager;

    private TextView update_room_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.room_choice, container, false);

        //build recycle view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.room_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        GameActivity.buildRoomFragmentCallback(this);
        GameActivity.apiRoom();

        ChestThread.setDoRun(false);

        update_room_list = (TextView) rootView.findViewById(R.id.update_room_list);
        update_room_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getActivity().getSupportFragmentManager();
                mAdapter = new RoomChoiceAdapter(fragmentManager,getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        return rootView;
    }


    @Override
    public void onRoomListReady() {
        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new RoomChoiceAdapter(fragmentManager,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}

