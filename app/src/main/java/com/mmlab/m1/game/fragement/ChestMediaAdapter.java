package com.mmlab.m1.game.fragement;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import com.mmlab.m1.R;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.module.UrlMediaPlayer;
import com.mmlab.m1.game.module.chestMedia;
import com.mmlab.m1.mini.MyApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class ChestMediaAdapter extends RecyclerView.Adapter<ChestMediaAdapter.MyViewHolder> {
    private List<chestMedia> mDataset;
    private MediaController mediaController;
    private static UrlMediaPlayer urlMediaPlayer;
    private RecyclerView mediaList;
    private Activity activity;
    private VideoView videoView;
    private ImageView bigImg;
    private ImageView close_bigImg_dialog;

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
    public ChestMediaAdapter(List<chestMedia> myDataset , Activity activity) {
        mDataset = myDataset;
        this.activity=activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChestMediaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_chest_media, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(mDataset != null){
            Log.d("OAO", " format : "+mDataset.get(position).getATT_format());
        }
        switch (mDataset.get(position).getATT_format()) {
            case "audio":
                holder.getImageView().setImageResource(R.drawable.ic_audio);
                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "play audio ....", Toast.LENGTH_LONG).show();
                        String url = mDataset.get(position).getATT_url();
                        palyAudio(url);
                    }
                });
                break;
            case "video":
                holder.getImageView().setImageResource(R.drawable.ic_movie_cyan_700_24dp);
                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataset.get(position).getATT_url()));
                        intent.setDataAndType(Uri.parse(mDataset.get(position).getATT_url()), "video/*");
                        activity.startActivity(intent);

                    }
                });

                break;
            case "image":
                holder.getImageView().setVisibility(View.VISIBLE);
                final String url = mDataset.get(position).getATT_url();
                Picasso.get().load(url).resize(85, 85).into(holder.getImageView());

                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(url), "image/*");

                        activity.startActivity(intent);
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
