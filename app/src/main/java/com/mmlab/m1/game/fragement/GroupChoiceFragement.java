package com.mmlab.m1.game.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.GroupFragmentCallback;
import com.mmlab.m1.game.thread.ChestThread;


public class GroupChoiceFragement extends Fragment implements GroupFragmentCallback
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentManager fragmentManager;

    private TextView score_url;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.group_choice, container, false);

        //build recycle view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.group_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        GameActivity.buildGroupFragmentCallback(this);
        GameActivity.apiGroup();

        ChestThread.setDoRun(false);

        score_url = (TextView) rootView.findViewById(R.id.score_url);
        score_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://deh.csie.ncku.edu.tw/extn"));
//                startActivity(browserIntent);


            }
        });

        return rootView;
    }


    @Override
    public void onGroupListReady() {
        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new GroupChoiceAdapter(fragmentManager,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}
