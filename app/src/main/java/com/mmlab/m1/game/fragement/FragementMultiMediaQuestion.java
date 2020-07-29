package com.mmlab.m1.game.fragement;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mmlab.m1.R;
import com.mmlab.m1.game.ApiService;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.PickItem;
import com.mmlab.m1.game.module.UrlMediaPlayer;
import com.mmlab.m1.game.module.chestMedia;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragementMultiMediaQuestion  extends Fragment {
    //ui
    private TextView question_text;
    private EditText text_answer;
    private RecyclerView upload_media_view;
    private RecyclerView question_media_view;
    private Button btn_take_img;
    private Button btn_take_video;
    private Button btn_take_audio;
    private Button btn_send;
    private Button btn_expound;
    //api
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(GameActivity.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiService api = retrofit.create(ApiService.class);
    Call<List<chestMedia>> chestMediaCall;
    //recycle view
    private RecyclerView.Adapter chestMediaAdapter;
    public static RecyclerView.Adapter answerMediaAdapter;
    LinearLayoutManager chestMediaManager;
    LinearLayoutManager answerMediaManager;
    //intent file
    private Uri fileUri;
    public static  List<PickItem> list_pick_media_uri =  new ArrayList();
    //
    String load_type = "image";
    String this_record_audio = "";
    //
    final int PICK_IMAGE_MULTIPLE = 99;
    final int PICK_AUDIO_MULTIPLE = 98;
    final int PICK_VIDEO_MULTIPLE = 97;
    final int TAKE_IMAGE_MULTIPLE = 96;
    final int TAKE_VIDEO_MULTIPLE = 95;

    FragmentManager fragmentManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_media_question, container, false);

        question_text = (TextView) rootView.findViewById(R.id.question);
        text_answer = (EditText) rootView.findViewById(R.id.text_answer);
        upload_media_view = (RecyclerView) rootView.findViewById(R.id.medai_answer);
        question_media_view = (RecyclerView) rootView.findViewById(R.id.media_question);
        btn_take_img = (Button) rootView.findViewById(R.id.btn_img);
        btn_take_video = (Button) rootView.findViewById(R.id.btn_video);
        btn_take_audio = (Button) rootView.findViewById(R.id.btn_audio);
        btn_send = (Button) rootView.findViewById(R.id.btn_send);
        btn_expound =  (Button) rootView.findViewById(R.id.btn_expound);

        chestMediaManager  = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        question_media_view.setLayoutManager(chestMediaManager);
        answerMediaManager  = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        upload_media_view.setLayoutManager(answerMediaManager);

        question_text.setText(GameActivity.getPick_chest_text());
        apiChestMedia(GameActivity.getPick_chest_id());

        fragmentManager = getActivity().getSupportFragmentManager();

        list_pick_media_uri.clear();

        text_answer.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (text_answer.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        btn_take_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dailog to show
//                final Dialog optionDialog = new Dialog(getActivity());
//                optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                optionDialog.setContentView(R.layout.dialog_chose_add_media);
//                optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
//                Button01.setText("選擇相片");
//                Button01.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pickPicture();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
//                Button02.setText("拍攝相片");
//                Button02.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        takePicture();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button06 = (Button) optionDialog.findViewById(R.id.btn_cancel);
//                Button06.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        optionDialog.dismiss();
//                    }
//                });
//                optionDialog.show();
                takePicture();
            }
        });
        btn_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialod to choose
//                final Dialog optionDialog = new Dialog(getActivity());
//                optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                optionDialog.setContentView(R.layout.dialog_chose_add_media);
//                optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
//                Button01.setText("選擇影片");
//                Button01.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pickVideo();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
//                Button02.setText("拍攝影片");
//                Button02.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        takeVideo();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button06 = (Button) optionDialog.findViewById(R.id.btn_cancel);
//                Button06.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        optionDialog.dismiss();
//                    }
//                });
//                optionDialog.show();
                takeVideo();
            }
        });
        btn_take_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog to choose
//                final Dialog optionDialog = new Dialog(getActivity());
//                optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                optionDialog.setContentView(R.layout.dialog_chose_add_media);
//                optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                Button Button01 = (Button) optionDialog.findViewById(R.id.btn_pick_function);
//                Button01.setText("選擇錄音");
//                Button01.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pickAudio();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button02 = (Button) optionDialog.findViewById(R.id.btn_take_function);
//                Button02.setText("錄製錄音");
//                Button02.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        takeAudio();
//                        optionDialog.dismiss();
//                    }
//                });
//                Button Button06 = (Button) optionDialog.findViewById(R.id.btn_cancel);
//                Button06.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        optionDialog.dismiss();
//                    }
//                });
//                optionDialog.show();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 3);
                }
                else{
                    showAudioSureDialog();
                }

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadSureDialog(""+text_answer.getText());
            }
        });

        return rootView;
    }

    public void apiChestMedia(int chest_id){
        chestMediaCall = api.getChestMedia(new Id(0,0,0,0,chest_id,0));
        chestMediaCall.enqueue(new Callback<List<chestMedia>>() {
            @Override
            public void onResponse(Call<List<chestMedia>> call, final Response<List<chestMedia>> response) {
                Log.d("OAO", "get chest media : " + response);
                if(response.body()!=null){
//                    Toast.makeText(getApplicationContext(), "chest media has media", Toast.LENGTH_LONG).show();
                    Log.d("OAO", "chest media size : " + response.body().size());
                    for(int i =0;i<response.body().size();i++){
                        if(response.body().get(i).getATT_format().equals("expound")){
                            final String expound_url = response.body().get(i).getATT_url();
                            btn_expound.setVisibility(View.VISIBLE);
                            btn_expound.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    palyAudio(expound_url);
                                }
                            });
                            Log.d("OAO", "remove chest media : " + i);
                            response.body().remove(i);
                            break;
                        }
                    }
                    Log.d("OAO", "new chest media size : " + response.body().size());
                    chestMediaAdapter = new ChestMediaAdapter(response.body(), getActivity());
                    if(question_media_view != null)
                        question_media_view.setAdapter(chestMediaAdapter);
                }
                else{
//                    Toast.makeText(getApplicationContext(), "chest media has no media", Toast.LENGTH_LONG).show();
                    Log.d("OAO", "chest media has no media");
            }
            }
            @Override
            public void onFailure(Call<List<chestMedia>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void palyAudio(String url){
        try {
            ChestMediaAdapter.closeAudio();
            UrlMediaPlayer urlMediaPlayer = new UrlMediaPlayer();
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

    public void takePicture(){
        if(list_pick_media_uri.size() < 5){
            if (getApplicationContext().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                // Open default camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, TAKE_IMAGE_MULTIPLE);

            } else {
                Toast.makeText(getActivity().getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getActivity(), "照片最多只能五張，請刪除不要的照片", Toast.LENGTH_LONG).show();
        }
    }

    public void takeVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath()+"videocapture_example.mp4");

        startActivityForResult(takeVideoIntent, TAKE_VIDEO_MULTIPLE);
    }

    public void takeAudio(){
        final MediaRecorder recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, "test");
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        File sdcard = Environment.getExternalStorageDirectory();
        String filepath = Environment.getExternalStorageDirectory().getPath();
        final File file = new File(filepath,"AudioRecorder");
        final String file_name = System.currentTimeMillis()  + ".mp3";
        if(!file.exists()){
            file.mkdirs();
        }
        recorder.setOutputFile(file.getAbsolutePath()  + file_name);
        try {
            recorder.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("錄音中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setButton("停止", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                this_record_audio = file.getAbsolutePath() + file_name;
                if(load_type != "audio")
                    load_type = "audio";
                list_pick_media_uri.clear();
                list_pick_media_uri.add(new PickItem(this_record_audio,"audio"));
                answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                upload_media_view.setAdapter(answerMediaAdapter);
                mProgressDialog.dismiss();
                recorder.stop();
                recorder.release();
            }
        });
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface p1) {
                recorder.stop();
                recorder.release();
            }
        });
        recorder.start();
        mProgressDialog.show();
    }

    public void pickPicture() {
        if(list_pick_media_uri.size() < 5){
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE_MULTIPLE);
        }
        else{
            Toast.makeText(getActivity(), "照片最多只能五張，請刪除不要的照片", Toast.LENGTH_LONG).show();
        }
    }

    public void pickVideo() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_VIDEO_MULTIPLE);
    }

    public void pickAudio(){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio "), PICK_AUDIO_MULTIPLE);
    }

    public static void update_upload_view() {
        answerMediaAdapter.notifyDataSetChanged();
    }

    public void apiUploadAnswerMedia(final String txt){
        final ProgressDialog nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Loading..");
        nDialog.setTitle("upload Answer");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
        File file;
        MultipartBody.Part file_part01 = null;
        MultipartBody.Part file_part02 = null;
        MultipartBody.Part file_part03 = null;
        MultipartBody.Part file_part04 = null;
        MultipartBody.Part file_part05 = null;
        if(list_pick_media_uri.size() >= 1){
            File curFile = new File(list_pick_media_uri.get(0).getUri());
            file_part01 = MultipartBody.Part.createFormData("file01", curFile.getName(), getRequestBody(curFile.getPath()));

        }
        if(list_pick_media_uri.size() >=2){
            File curFile = new File(list_pick_media_uri.get(1).getUri());
            file_part02 = MultipartBody.Part.createFormData("file02", curFile.getName(), getRequestBody(curFile.getPath()));
        }
        if(list_pick_media_uri.size() >=3){
            File curFile = new File(list_pick_media_uri.get(2).getUri());
            file_part03 = MultipartBody.Part.createFormData("file03", curFile.getName(), getRequestBody(curFile.getPath()));

        }
        if(list_pick_media_uri.size() >=4){
            File curFile = new File(list_pick_media_uri.get(3).getUri());
            file_part04 = MultipartBody.Part.createFormData("file04", curFile.getName(), getRequestBody(curFile.getPath()));

        }
        if(list_pick_media_uri.size() >=5){
            File curFile = new File(list_pick_media_uri.get(4).getUri());
            file_part05 = MultipartBody.Part.createFormData("file05", curFile.getName(), getRequestBody(curFile.getPath()));

        }
        if(true) {
            RequestBody txt_part = RequestBody.create(MultipartBody.FORM, txt);
            RequestBody chest_id_part = RequestBody.create(MultipartBody.FORM, "" + GameActivity.getPick_chest_id());
            RequestBody user_id_part = RequestBody.create(MultipartBody.FORM, "" + GameActivity.getUser_id());
            RequestBody game_id_part = RequestBody.create(MultipartBody.FORM, "" + GameActivity.getGame_id());
            RequestBody lat_part = RequestBody.create(MultipartBody.FORM, "" + GameActivity.getLat());
            RequestBody lng_part = RequestBody.create(MultipartBody.FORM, "" + GameActivity.getLng());
            RequestBody point_part = RequestBody.create(MultipartBody.FORM, ""+0);
            Log.d("msg", "ready to call api");
            Call thiscall = api.uploadMediaAnswer(file_part01,file_part02,file_part03,file_part04,file_part05, txt_part, chest_id_part, user_id_part, game_id_part, lat_part, lng_part, point_part);
            thiscall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getActivity(), "上傳成功", Toast.LENGTH_LONG).show();
                    list_pick_media_uri.clear();
                    nDialog.dismiss();
                    for (int i = fragmentManager.getBackStackEntryCount() - 1; i > 0; i--) {
                        Log.d("frag", fragmentManager.getBackStackEntryAt(i).getName());
                        if (!fragmentManager.getBackStackEntryAt(i).getName().equalsIgnoreCase("RoomFragment")) {
                            fragmentManager.popBackStack();
                        }
                        else
                        {
                            break;
                        }
                    }
                    Log.d("msg", "ok");
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), "上傳失敗", Toast.LENGTH_LONG).show();
                    nDialog.dismiss();
                    Log.d("msg", "not ok");
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "上傳格式錯誤", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public boolean isAudioFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("audio");
    }

    public RequestBody getRequestBody(String path){
        File file = new File(path);
        String extension = FilenameUtils.getExtension(path);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/"+ extension), file);
        if (isImageFile(path)) {
            fileReqBody = RequestBody.create(MediaType.parse("image/"+ extension), file);
        }
        else if (isVideoFile(path)) {
            fileReqBody = RequestBody.create(MediaType.parse("video/"+ extension), file);
        }
        else if (isAudioFile(path)) {
            fileReqBody = RequestBody.create(MediaType.parse("audio/"+ extension), file);
        }
        return fileReqBody;
    }

    private void showUploadSureDialog(final String txt){
        final Dialog tempdialog = new Dialog(getActivity());
        tempdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tempdialog.setContentView(R.layout.alert_dialog_2btn);
        tempdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title_text = (TextView) tempdialog.findViewById(R.id.title_text);
        title_text.setText("確定要上傳答案");

        Button left_btn = (Button) tempdialog.findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg", GameActivity.getPick_chest_id() + "upload media");
                apiUploadAnswerMedia(txt);
                tempdialog.dismiss();
            }
        });

        Button right_btn = (Button) tempdialog.findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdialog.dismiss();
            }
        });

        tempdialog.show();
    }

    private void showAudioSureDialog(){
        final Dialog tempdialog = new Dialog(getActivity());
        tempdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tempdialog.setContentView(R.layout.alert_dialog_2btn);
        tempdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title_text = (TextView) tempdialog.findViewById(R.id.title_text);
        title_text.setText("是否開始錄音");

        Button left_btn = (Button) tempdialog.findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAudio();
                tempdialog.dismiss();
            }
        });

        Button right_btn = (Button) tempdialog.findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdialog.dismiss();
            }
        });

        tempdialog.show();
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data)
            {
                //check game time
                MapFragement.apiUpdateGameData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String dataPath = cursor.getString(columnIndex);
                cursor.close();

                if(load_type != "image"){
                    load_type = "image";
                    list_pick_media_uri.clear();
                }
                list_pick_media_uri.add(new PickItem(dataPath,"image"));
                answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                upload_media_view.setAdapter(answerMediaAdapter);
            }
            else if(requestCode == PICK_AUDIO_MULTIPLE && resultCode == RESULT_OK && null != data)
            {
                //check game time
                MapFragement.apiUpdateGameData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String dataPath = cursor.getString(columnIndex);
                cursor.close();

                if(load_type != "audio")
                    load_type = "audio";
                list_pick_media_uri.clear();
                list_pick_media_uri.add(new PickItem(dataPath,"audio"));
                answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                upload_media_view.setAdapter(answerMediaAdapter);
            }
            else if(requestCode == PICK_VIDEO_MULTIPLE && resultCode == RESULT_OK && null != data)
            {
                //check game time
                MapFragement.apiUpdateGameData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String dataPath = cursor.getString(columnIndex);
                cursor.close();

                if(load_type != "video")
                    load_type = "video";
                list_pick_media_uri.clear();
                list_pick_media_uri.add(new PickItem(dataPath,"video"));
                answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                upload_media_view.setAdapter(answerMediaAdapter);
            }
            else if (requestCode == TAKE_IMAGE_MULTIPLE && resultCode == RESULT_OK) {
                //check game time
                MapFragement.apiUpdateGameData();

                //get data path
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String dataPath = cursor.getString(columnIndex);
                cursor.close();

                //exif the data to upload image type
                File ouputFile = null;
                try {
                    //exif
                    Bitmap bitmap = BitmapFactory.decodeFile(dataPath);
                    Bitmap rotatedBitmap;
                    ExifInterface exif = new ExifInterface(dataPath);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhysicsSketchpad";
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    ouputFile = new File(dir, randomString()+".JPEG");
                    FileOutputStream fOut = new FileOutputStream(ouputFile);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
                    fOut.flush();
                    fOut.close();



                    //check load path
                    if(load_type != "image"){
                        load_type = "image";
                        list_pick_media_uri.clear();
                    }

                    //update the recycleview
                    list_pick_media_uri.add(new PickItem(ouputFile.getPath(),"image"));
                    answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                    upload_media_view.setAdapter(answerMediaAdapter);

                }catch(IOException ex) {
                    Log.e(TAG, "Failed to get Exif data", ex);
                }


            }
            else if(requestCode == TAKE_VIDEO_MULTIPLE && resultCode == RESULT_OK && null != data)
            {
                //check game time
                MapFragement.apiUpdateGameData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String dataPath = cursor.getString(columnIndex);
                cursor.close();

                if(load_type != "video")
                    load_type = "video";
                list_pick_media_uri.clear();
                list_pick_media_uri.add(new PickItem(dataPath,"video"));
                answerMediaAdapter = new AnswerMediaAdapter(list_pick_media_uri, getActivity());
                upload_media_view.setAdapter(answerMediaAdapter);
            }
            else {
//                Toast.makeText(getApplicationContext(), "no action", Toast.LENGTH_LONG).show();
            }
    }

    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 7;
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
