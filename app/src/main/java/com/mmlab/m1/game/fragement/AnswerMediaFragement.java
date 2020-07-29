package com.mmlab.m1.game.fragement;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mmlab.m1.R;

public class AnswerMediaFragement extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_media, container, false);


        TextView text = (TextView) rootView.findViewById(R.id.question_context);
        FloatingActionButton soundButton = (FloatingActionButton)rootView.findViewById(R.id.sound_player);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_loader);
        RecyclerView mediaList = (RecyclerView) rootView.findViewById(R.id.media_list);
        Button closeDialog = (Button) rootView.findViewById(R.id.close_dialog);
        EditText media_txt = (EditText) rootView.findViewById(R.id.discribe_text);
        Button btn_upload_media = (Button) rootView.findViewById(R.id.btn_upload_media);

        Button upload_media = (Button) rootView.findViewById(R.id.upload_media);
        upload_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog optionDialog = new Dialog(getActivity());
                optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                optionDialog.setContentView(R.layout.dialog_chose_media);
                optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                Button Button01 = (Button) optionDialog.findViewById(R.id.btn_take_pic);
                Button01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        clickPicture();
                        optionDialog.dismiss();
                    }
                });

                Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_vid);
                Button02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        clickVideo();
                        optionDialog.dismiss();
                    }
                });

                Button Button03 = (Button) optionDialog.findViewById(R.id.btn_take_aud);
                Button03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        browseAudio();
                        optionDialog.dismiss();
                    }
                });

                Button Button04 = (Button) optionDialog.findViewById(R.id.btn_chose_pic);
                Button04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        browsePicture();
                        optionDialog.dismiss();
                    }
                });

                Button Button05 = (Button) optionDialog.findViewById(R.id.btn_chose_vid);
                Button05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        browseVideo();
                        optionDialog.dismiss();
                    }
                });

                Button Button06 = (Button) optionDialog.findViewById(R.id.btn_cancel_take);
                Button06.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionDialog.dismiss();
                    }
                });

                Button Button07 = (Button) optionDialog.findViewById(R.id.btn_take_audio);
                Button07.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        recordAudio("123");
                    }
                });

                optionDialog.show();
            }
        });

        return rootView;
    }

}
