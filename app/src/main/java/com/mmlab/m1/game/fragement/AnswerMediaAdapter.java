package com.mmlab.m1.game.fragement;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.module.PickItem;
import com.mmlab.m1.game.module.UrlMediaPlayer;
import com.mmlab.m1.game.module.chestMedia;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AnswerMediaAdapter extends RecyclerView.Adapter<AnswerMediaAdapter.MyViewHolder> {
    private List<PickItem> mDataset;
    private static UrlMediaPlayer urlMediaPlayer;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }


        public ImageView imageView;


        public MyViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.img_and_audio_view);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnswerMediaAdapter(List<PickItem> myDataset , Activity activity) {
        mDataset = myDataset;
        this.activity=activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AnswerMediaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_answer_media, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(mDataset != null){
            Log.d("OAO", " format : "+mDataset.get(position));
        }
        switch (mDataset.get(position).getType()) {
            case "audio":
                holder.getImageView().setImageResource(R.drawable.ic_audio);
                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog optionDialog = new Dialog(activity);
                        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        optionDialog.setContentView(R.layout.dialog_chose_add_media);
                        optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
                        Button01.setText("預覽錄音");
                        Button01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(activity, "播放中 ....", Toast.LENGTH_LONG).show();
                                String url = mDataset.get(position).getUri();
                                palyAudio(url);
                                optionDialog.dismiss();
                            }
                        });
                        Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
                        Button02.setText("刪除錄音");
                        Button02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragementMultiMediaQuestion.list_pick_media_uri.remove(position);
                                FragementMultiMediaQuestion.update_upload_view();
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
                break;
            case "video":
                holder.getImageView().setImageResource(R.drawable.ic_movie_cyan_700_24dp);
                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog optionDialog = new Dialog(activity);
                        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        optionDialog.setContentView(R.layout.dialog_chose_add_media);
                        optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
                        Button01.setText("預覽影片");
                        Button01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataset.get(position).getUri()));
                                intent.setDataAndType(Uri.parse(mDataset.get(position).getUri()), "video/*");
                                activity.startActivity(intent);
                                optionDialog.dismiss();
                            }
                        });
                        Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
                        Button02.setText("刪除影片");
                        Button02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragementMultiMediaQuestion.list_pick_media_uri.remove(position);
                                FragementMultiMediaQuestion.update_upload_view();
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

                break;
            case "image":
                holder.getImageView().setVisibility(View.VISIBLE);
                final String url = mDataset.get(position).getUri();
                File f = new File(url);
                Picasso.get().load(f).fit().into(holder.getImageView());
                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog optionDialog = new Dialog(activity);
                        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        optionDialog.setContentView(R.layout.dialog_chose_add_media);
                        optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
                        Button01.setText("預覽圖片");
                        Button01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataset.get(position).getUri()));
                                intent.setDataAndType(Uri.parse(mDataset.get(position).getUri()), "image/*");
                                activity.startActivity(intent);
                                optionDialog.dismiss();
                            }
                        });
                        Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
                        Button02.setText("刪除圖片");
                        Button02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragementMultiMediaQuestion.list_pick_media_uri.remove(position);
                                FragementMultiMediaQuestion.update_upload_view();
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

                break;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void palyAudio(String url){
        try {
            closeAudio();
            MapFragement.closeAudio();
            urlMediaPlayer = new UrlMediaPlayer();
            urlMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            urlMediaPlayer.setDataSource(url);
            urlMediaPlayer.prepare();
            urlMediaPlayer.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void closeAudio(){
        if(urlMediaPlayer!=null){
            urlMediaPlayer.release();
        }
    }
}
