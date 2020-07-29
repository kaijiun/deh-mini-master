package com.mmlab.m1.game.fragement;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.module.MemberPoint;

import java.util.List;

public class AdapterShowPoint extends RecyclerView.Adapter<AdapterShowPoint.MyViewHolder> {

    List<MemberPoint> data;
    Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public TextView getTextView1() { return textView1; }
        public TextView getTextView2() { return textView2; }
        public TextView getTextView3() { return textView3; }


        public MyViewHolder(View v) {
            super(v);
            textView1 = (TextView) v.findViewById(R.id.tv_show_point_name);
            textView2 = (TextView) v.findViewById(R.id.tv_show_point_point);
            textView3 = (TextView) v.findViewById(R.id.tv_show_point_time);
        }

    }

    public AdapterShowPoint(List<MemberPoint> data, Activity activity){
        this.data = data;
        this.activity = activity;
    }

    @Override
    public AdapterShowPoint.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_show_point, parent, false);
        AdapterShowPoint.MyViewHolder vh = new AdapterShowPoint.MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterShowPoint.MyViewHolder holder, int position) {
        holder.getTextView1().setText(data.get(position).getNickname());
        holder.getTextView2().setText(""+data.get(position).getPoint());

        String[] tokens = data.get(position).getAnswer_time().split("T");
        String[] tokens2 =  tokens[1].split("\\.");
        String[] tokens3 =  tokens2[0].split(":");

        holder.getTextView3().setText(tokens[0]+ " " +tokens3[0] + ":" + tokens3[1] + ":" + tokens3[2] );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}