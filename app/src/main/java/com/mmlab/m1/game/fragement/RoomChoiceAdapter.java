package com.mmlab.m1.game.fragement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.OnRoomGameStatusText;
import com.mmlab.m1.game.callback.RoomAdapterCallback;
import com.mmlab.m1.game.module.Room;
import com.mmlab.m1.game.thread.ChestThread;

import java.util.List;

public class RoomChoiceAdapter  extends RecyclerView.Adapter<GroupChoiceAdapter.ViewHolder> implements RoomAdapterCallback, OnRoomGameStatusText {
    private List<Room> roomDataset;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MapFragement mapFragement;


    private Activity activity;
    private int groupLeaderId;

    int room_id;



    @Override
    public void returnText(Button button, String text, String room_name) {



        button.setText(room_name + "(遊戲" + text + ")");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button roomBtn ;

        public ViewHolder(View v) {
            super(v);
            roomBtn = (Button) v.findViewById(R.id.group_btn);
        }
        public Button getButton() {
            return roomBtn;
        }

    }

    public RoomChoiceAdapter(FragmentManager fm, Activity a) {
        roomDataset = GameActivity.getUser_room_list();
        fragmentManager = fm;
        activity = a;

        GameActivity.buildRoomAdapterCallback(this);
        GameActivity.buildOnRoomGameStatusText(this);
    }

    @Override
    public void onRoomHasNoGame(final Button button, final int room_id,final String room_name) {
        GameActivity.apiRoomStatusText(button,room_id,room_name);
        //group has no game
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_1btn);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title_text = (TextView) dialog.findViewById(R.id.title_text);
        title_text.setText("尚未設定遊戲");
        Button bottom_btn = (Button) dialog.findViewById(R.id.bottom_btn);
        bottom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onRoomIsGaming(final Button button, final int room_id,final String group_name) {
        GameActivity.apiRoomStatusText(button,room_id,group_name);
        GameActivity.apiGameData();
    }

    @Override
    public void onRoomUnstartGame(final Button button, final int room_id,final String group_name) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_2btn);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView title_text = (TextView) dialog.findViewById(R.id.title_text);
        title_text.setText("是否啟動遊戲");

        Button leftButton = (Button) dialog.findViewById(R.id.left_btn);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check is leader
                groupLeaderId =  GameActivity.getGroup_leader_id();
                if(groupLeaderId == GameActivity.getUser_id()){
                    GameActivity.apiGameStart(button,room_id,group_name);
                }
                else{
                    Toast.makeText(activity, "只有組頭才有權利啟動遊戲！", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });
        Button rightButton = (Button) dialog.findViewById(R.id.right_btn);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onGameDataReady() {
        ChestThread.setDoRun(true);
        fragmentTransaction = fragmentManager.beginTransaction();
        mapFragement = new MapFragement();
        fragmentTransaction.replace(R.id.main_fragment,mapFragement);
        fragmentTransaction.addToBackStack("RoomFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onStartRoomGame(Button button,int room_id,String group_name){
        GameActivity.apiRoomStatusText(button,room_id,group_name);
        Toast.makeText(activity, "遊戲啟動中 ......", Toast.LENGTH_LONG).show();
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
        holder.getButton().setText(" " + roomDataset.get(position).getRoom_name() );
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set room id
                room_id = roomDataset.get(position).getId();
                GameActivity.setRoom_id(room_id);

                //dialog
                final Dialog optionDialog = new Dialog(activity);
                optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                optionDialog.setContentView(R.layout.dialog_chose_add_media);
                optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
                Button01.setText("進入遊戲");
                Button01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameActivity.apiRoomStatus(holder.getButton(), roomDataset.get(position).getId(), roomDataset.get(position).getRoom_name());
                        optionDialog.dismiss();
                    }
                });
                Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
                Button02.setText("查看成績");
                Button02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction;
                        FragementGameHistory fragementGameHistory = new FragementGameHistory();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment, fragementGameHistory);
                        fragmentTransaction.addToBackStack("RoomFragment");
                        fragmentTransaction.commit();
                        optionDialog.dismiss();
                    }
                });
                Button Button06 = (Button) optionDialog.findViewById(R.id.btn_cancel);
                Button06.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionDialog.dismiss();
                    }
                });

                optionDialog.show();

            }
        });
        GameActivity.apiRoomStatusText(holder.getButton(),roomDataset.get(position).getId(),roomDataset.get(position).getRoom_name());
    }

    @Override
    public int getItemCount() {
        return roomDataset.size();
    }




}
