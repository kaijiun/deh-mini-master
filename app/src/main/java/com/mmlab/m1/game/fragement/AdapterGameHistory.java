package com.mmlab.m1.game.fragement;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.module.HistoryGame;

import java.util.List;

public class AdapterGameHistory extends RecyclerView.Adapter<AdapterGameHistory.MyViewHolder> {

    List<HistoryGame> data;
    FragmentManager fragmentManager;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public MyViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.tv_game_title_start_time);
        }

    }

    public AdapterGameHistory(List<HistoryGame> data, FragmentManager fragmentManager){
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public AdapterGameHistory.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_game_history, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String[] tokens = data.get(position).getStart_time().split("T");
        String[] tokens2 =  tokens[1].split("\\.");
        String[] tokens3 =  tokens2[0].split(":");

        holder.getTextView().setText(tokens[0]+ " " +tokens3[0] + ":" + tokens3[1] );
        holder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.setGame_id(data.get(position).getId());

                FragmentTransaction fragmentTransaction;
                FragementShowPoint fragementGameHistory = new FragementShowPoint();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, fragementGameHistory);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
