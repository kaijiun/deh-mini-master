package com.mmlab.m1.mini;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.melnykov.fab.FloatingActionButton;
import com.mmlab.m1.R;
import com.mmlab.m1.game.ApiService;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.Room;
import com.mmlab.m1.game.module.User;
import com.mmlab.m1.mini.constant.PLAYBACK;
import com.mmlab.m1.mini.model.CountClickRequestObj;
import com.mmlab.m1.mini.model.LOISequenceModel;
import com.mmlab.m1.mini.model.MyFavorite;
import com.mmlab.m1.mini.model.POIModel;
import com.mmlab.m1.mini.network.ProxyService;
import com.mmlab.m1.mini.helper.Preset;
import com.mmlab.m1.mini.save_data.SavePOI;
import com.mmlab.m1.mini.service.HttpAsyncTask;
import com.mmlab.m1.mini.service.TaskCompleted;
import com.mmlab.m1.mini.service.retrofitAPI;
import com.mmlab.m1.mini.widget.AudioPlayer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mmlab.m1.game.GameActivity.getApi;

public class POIActivity extends AppCompatActivity implements TaskCompleted {

    private MyApplication globalVariable;

    private String keyword;
    private String type;
    private POIModel model;
    private LOISequenceModel loiSequence;
    private ImageView mPhoto;
    private TextView mSubject;
    private TextView mType1;
    private TextView mKeyword;
    private TextView mAddress;
    private TextView mInfo;
    private TextView mViewtime;
    private FloatingActionButton mAlbum;
    private FloatingActionButton mAudio;
    private FloatingActionButton mMovie;
    private FloatingActionButton mAudioTour;
    private FABProgressCircle fabProgressCircle;
    private AudioPlayer audioPlayer;
    private android.support.design.widget.FloatingActionButton mLike;
    private Realm realm;
    private OkHttpClient client = new OkHttpClient();
    private ProxyService mServer = null;
    private ServerReceiver serverReceiver = null;
    private CollapsingToolbarLayout collapsingToolbar;
    String uuid = UUID.randomUUID().toString();
    String userAccount = "";
    String clicked_poi_id = "";
    String account_login = "";
    String ula = "";
    String ulo = "";
    String action = "";
    String where_from = "";


    TextView viewtimeTextView;

    private  String BASE_URL = "http://deh.csie.ncku.edu.tw:8080/";
    //private static String BASE_URL = "http://140.116.82.130:8080/";
    private  Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private  retrofitAPI api = retrofit.create(retrofitAPI.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        globalVariable = (MyApplication) getApplicationContext();
        realm = Realm.getInstance(getApplicationContext());

        mPhoto = (ImageView) findViewById(R.id.imageView_poi);
        mSubject = (TextView) findViewById(R.id.subjectTextView);
        mType1 = (TextView) findViewById(R.id.type1TextView);
        mKeyword = (TextView) findViewById(R.id.keywordTextView);
        mAddress = (TextView) findViewById(R.id.addressTextView);
        mInfo = (TextView) findViewById(R.id.infoTextView);
        mViewtime = (TextView) findViewById(R.id.viewtimeTextView);

        mLike = (android.support.design.widget.FloatingActionButton) findViewById(R.id.like);
        mAlbum = (FloatingActionButton) findViewById(R.id.album);
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        mAudio = (FloatingActionButton) findViewById(R.id.audio);
        mMovie = (FloatingActionButton) findViewById(R.id.movie);
        mAudioTour = (FloatingActionButton) findViewById(R.id.audio_tour);

        viewtimeTextView = (TextView) findViewById(R.id.viewtimeTextView);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        clicked_poi_id = getIntent().getStringExtra("id");
        ula = getIntent().getStringExtra("ula");
        ulo =  getIntent().getStringExtra("ulo");
        account_login =  getIntent().getStringExtra("account");
        uuid = getIntent().getStringExtra("uuid");
        //where_from = getIntent().getStringExtra("where_from");
        /*Log.d("id",String.valueOf(clicked_poi_id) );
        Log.d("ula",String.valueOf(ula) );
        Log.d("ulo",String.valueOf(ulo) );
        Log.d("uuid",String.valueOf(uuid) );
        Log.d("account" , account_login);*/
        uploadLog(where_from,account_login,ula,ulo,action,clicked_poi_id,uuid);
        if (type != null) {
            if (type.equals("POI-Id")) {
                loiSequence = getIntent().getParcelableExtra("POI-Content");

                String api = getResources().getString(R.string.api_poi_id);
                String url = null;

                if (loiSequence != null) {
                    collapsingToolbar.setTitle(loiSequence.getPOIName());
                    url = api +/* "id=" +*/ loiSequence.getPOIId();
                } else {
                    collapsingToolbar.setTitle(bundle.getString("title"));
                    url = api +/* "id=" +*/ bundle.getString("id");
                }

                Log.d("url87", url);
                new HttpAsyncTask(this, type,1, MiniActivity.getToken()).execute(url);

            } else {
                model = getIntent().getParcelableExtra("POI-Content");
                collapsingToolbar.setTitle(model.getPOIName());
                loadData();
            }
        }


        apiGetCountClick(Integer.valueOf(clicked_poi_id));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi, menu);

        //　如果當前是群組導覽功能就除去地圖功能
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String response, String type) {
        Log.d("response", response);
        SavePOI savePOI = new SavePOI();
        savePOI.setType("Fav");
        savePOI.parsePoiListJSONObject(response);
        model = savePOI.getPOIList().get(0);
        ProxyService proxyService = new ProxyService();
        proxyService.sendSinglePOI(model);
        loadData();
    }


    private void loadData() {
        collapsingToolbar.setTitle(model.getPOIName());

        mSubject.setText(model.getPOISubject());
        mType1.setText(model.getPOIType1());

        for (String i : model.getPOIKeywords()) {
            keyword = i;
        }

        mKeyword.setText(keyword);

        mAddress.setText(model.getPOIAddress());
        mInfo.setText(model.getPOIInfo());

        final RealmResults<MyFavorite> result = realm.where(MyFavorite.class)
                .equalTo("id", model.getPOIId())
                .findAll();

        if (!result.isEmpty()) {
            mLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_pink_300)));
            mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (result.isEmpty()) {
                    realm.beginTransaction();
                    final MyFavorite myFavorite = realm.createObject(MyFavorite.class);

                    myFavorite.setId(model.getPOIId());
                    myFavorite.setTitle(model.getPOIName());
                    myFavorite.setInfo(model.getPOIInfo());
                    myFavorite.setAddress(model.getPOIAddress());
                    myFavorite.setFavlat(model.getPOILat());
                    myFavorite.setFavlong(model.getPOILong());
                    if (!model.getPOIPics().isEmpty())
                        myFavorite.setPic(model.getPOIPics().get(0));

                    realm.commitTransaction();

                    mLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_pink_300)));
                    mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));

                    Snackbar.make(mInfo, getString(R.string.add_to_favorite), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();

                } else {
                    realm.beginTransaction();
                    MyFavorite myFavorite = result.get(0);
                    myFavorite.removeFromRealm();
                    realm.commitTransaction();

                    mLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_outline_pink_200_24dp));

                    globalVariable.setFavoriteRemove(true);

                    Snackbar.make(mInfo, getString(R.string.cancel_add_to_favorite), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();
                }


            }
        });


        if (model.getPOIPics().size() != 0) {
            Glide.with(this).load(model.getPOIPics().get(0)).into(mPhoto);

            mAlbum.setVisibility(View.VISIBLE);
            mAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mServer.sendPhoto();


                    Intent intent = new Intent(POIActivity.this, AlbumActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Pics", model.getPOIPics());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, UPDATE_PHOTO);
                }
            });

        }

        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.loading_progress);
        final FrameLayout mFrame = (FrameLayout) findViewById(R.id.frame);
        if (model.getPOIAudios().size() != 0 ) {
            mAudio.setVisibility(View.VISIBLE);
            mAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//					if(audioPlayer==null){
//						audioPlayer = new AudioPlayer(POIActivity.this, mAudio, progressView, fabProgressCircle, item.getPOIAudios().get(0));
//					}
//					else{
//						audioPlayer.playAudio();
//					}
                    Intent intent = new Intent(POIActivity.this, VideoDemoActivity.class);
                    PLAYBACK.remoteUri = model.getPOIAudios().get(0).replace("moe2//", "");
                    startActivityForResult(intent, UPDATE);
                }
            });

//			mAudio.setOnLongClickListener(new View.OnLongClickListener() {
//				@Override
//				public boolean onLongClick(View v) {
//					if(audioPlayer!=null){
//						audioPlayer.stopAudio();
//					}
//					return true;
//				}
//			});

        } else {
            mFrame.setVisibility(View.GONE);
        }

        if (model.getPOIPMovies().size() != 0 ) {
            mMovie.setVisibility(View.VISIBLE);
            mMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(POIActivity.this, VideoDemoActivity.class);
                    PLAYBACK.remoteUri = model.getPOIPMovies().get(0).replace("moe2//", "");
                    startActivityForResult(intent, UPDATE);
                }
            });
        } else {
            mMovie.setVisibility(View.GONE);
        }

        if (model.getPOIAudioTours().size() != 0 ) {
            mAudioTour.setVisibility(View.VISIBLE);
            mAudioTour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(POIActivity.this, VideoDemoActivity.class);
                    PLAYBACK.remoteUri = model.getPOIAudioTours().get(0).replace("moe2//", "");
                    startActivityForResult(intent, UPDATE);
                }
            });

        } else {
            mAudioTour.setVisibility(View.GONE);
        }

    }

    ServiceConnection serverConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
//			Toast.makeText(POIActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mServer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
//			Toast.makeText(POIActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            ProxyService.ProxyBinder mLocalBinder = (ProxyService.ProxyBinder) service;
            mServer = mLocalBinder.getProxyInstance();
        }
    };


    public class ServerReceiver extends BroadcastReceiver {

        public ServerReceiver() {

        }

        public void onReceive(Context context, Intent intent) {
            if (ProxyService.GETPOI_ACTION.equals(intent.getAction())) {

            } else if (ProxyService.MEMBER_ACTION.equals(intent.getAction())) {

            } else if (ProxyService.FILE_COMPLETE__ACTION.equals(intent.getAction())) {
//				updateImagesList();
//				Toast.makeText(getApplicationContext(), "Proxy" + intent.getStringExtra("file"), Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 處理來自Service的broadcast訊息
     * 交由pHandler處理費時任務和mHandler更新UI介面
     * 避免阻塞主線程
     */


    public static final int UPDATE = 0;
    public static final int VIDEO = 1;
    public static final int PHOTO = 2;
    public static final int UPDATE_PHOTO = 3;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case UPDATE_PHOTO:
                break;
            case VIDEO:
                Intent intent = new Intent(POIActivity.this, VideoDemoActivity.class);
//              PLAYBACK.remoteUri = model.getPOIPMovies().get(0).replace("moe2//", "");
                intent.putExtra("data", intent.getIntExtra("data", 0));
                startActivityForResult(intent, VIDEO);
                break;
            case PHOTO:
                intent = new Intent(getApplicationContext(), AlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pics", model.getPOIPics());
                intent.putExtras(bundle);
                startActivityForResult(intent, PHOTO);
                break;
            default:
        }
    }

    protected void onStart() {
        super.onStart();
        Preset.loadPreferences(getApplicationContext());

        /*if (Preset.loadPreferences(getApplicationContext()) == IDENTITY.MEMBER) {
            Intent intent = new Intent(POIActivity.this, MemberService.class);
            startService(intent);
            Intent intent1 = new Intent(POIActivity.this, MemberService.class);
            bindService(intent1, clientConnection, BIND_AUTO_CREATE);

            clientReceiver = new ClientReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MemberService.CONNECT_ACTION);
            intentFilter.addAction(MemberService.FILE_COMPLETE__ACTION);
            intentFilter.addAction(MemberService.PHOTO_START_ACTION);
            registerReceiver(clientReceiver, intentFilter);
            // register
            intentFilter = new IntentFilter();
            intentFilter.addAction(MemberService.VIDEO_START_ACTION);
            registerReceiver(serviceReceiver, intentFilter);*/
        Intent intent = new Intent(POIActivity.this, ProxyService.class);
        startService(intent);
        Intent intent1 = new Intent(POIActivity.this, ProxyService.class);
        bindService(intent1, serverConnection, BIND_AUTO_CREATE);

        serverReceiver = new ServerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ProxyService.GETPOI_ACTION);
        intentFilter.addAction(ProxyService.FILE_COMPLETE__ACTION);
        registerReceiver(serverReceiver, intentFilter);



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioPlayer != null) {
            audioPlayer.restAudio();
        }
        /*if (Preset.loadPreferences(getApplicationContext()) == IDENTITY.MEMBER) {
            unbindService(clientConnection);
            unregisterReceiver(clientReceiver);
            // unregister
            unregisterReceiver(serviceReceiver);
        } else */
        unbindService(serverConnection);
        unregisterReceiver(serverReceiver);

    }
    public void uploadLog(String where_from,String userAccount , String ula , String ulo , String action , String clicked_poi_id , String uuid){

        //action要調
        FormBody.Builder formBody = new FormBody.Builder();
        /*formBody.add("useraccount", userAccount);
        formBody.add("ula", ula);
        formBody.add("ulo", ulo);
        formBody.add("action", "/API/test/poi_detail/" + clicked_poi_id);
        formBody.add("devid", uuid);*/
        String url = "http://deh.csie.ncku.edu.tw:8080/api/v1/users/xoilog?useraccount=" + userAccount + "&ula=" + ula + "&ulo=" + ulo + "&devid=" + uuid + "&action=" + "/API/poi_detail/" + clicked_poi_id;
        Request requestt = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        Log.d("URL", url);
        client.newCall(requestt).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", "Failed Log");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("OKHTTP", "Success Log");
            }
        });

    }

    public void apiGetCountClick(int poi_id){
        retrofit2.Call<ResponseBody> callGetCountClick = api.getCountClick(new CountClickRequestObj(poi_id));
        callGetCountClick.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call2, retrofit2.Response<ResponseBody> response) {
                Log.d("vvvvv", "call callGetCountClick sucess");
                Log.d("vvvvv", "response : " + model.getPOIId());
                //Log.d("vvvvv", "response : " + model.);

                if(response.body()!=null) {
                    Log.d("vvvvv", "response : " + response.body().source());
                    String str = response.body().source().toString();
                    String[] tokens = str.split(":");
                    String str2 = tokens[1];
                    String[] tokens2 = str2.split("\\}");

                    viewtimeTextView.setText(tokens2[0]);

                }
                else{
                    Log.d("vvvvv", "call callGetCountClick null");
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }



}

