package com.mmlab.m1.game.fragement;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.module.Group;

import java.util.List;


public class GroupChoiceAdapter extends RecyclerView.Adapter<GroupChoiceAdapter.ViewHolder> {
    private List<Group> mDataset;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RoomChoiceFragement roomFragement;


    private Activity activity;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button groupBtn ;

        public ViewHolder(View v) {
            super(v);
            groupBtn = (Button) v.findViewById(R.id.group_btn);
        }
        public Button getButton() {
            return groupBtn;
        }

    }

    public GroupChoiceAdapter(FragmentManager fm, Activity a) {
        mDataset = GameActivity.getUser_group_list();
        fragmentManager = fm;
        activity = a;

    }


    @Override
    public GroupChoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_choice_container, parent, false);
        GroupChoiceAdapter.ViewHolder vh = new GroupChoiceAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final GroupChoiceAdapter.ViewHolder holder, final int position) {
        //button
        holder.getButton().setText(" " + mDataset.get(position).getGroup_name() );
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set group id
                Log.d("OAO", "pick group = " + mDataset.get(position).getGroup_id());
                GameActivity.setGroup_id(mDataset.get(position).getGroup_id());
                GameActivity.setGroup_leader_id(mDataset.get(position).getGroup_leader_id());

                fragmentTransaction = fragmentManager.beginTransaction();
                roomFragement = new RoomChoiceFragement();
                fragmentTransaction.replace(R.id.main_fragment,roomFragement);
                fragmentTransaction.addToBackStack("GroupFragment");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }




}
