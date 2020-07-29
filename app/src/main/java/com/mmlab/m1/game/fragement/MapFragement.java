package com.mmlab.m1.game.fragement;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.mmlab.m1.R;
import com.mmlab.m1.game.ApiService;
import com.mmlab.m1.game.GameActivity;
import com.mmlab.m1.game.callback.OnChestReadyCallback;
import com.mmlab.m1.game.db.DbHandler;
import com.mmlab.m1.game.module.AnswerRecord;
import com.mmlab.m1.game.module.AnswerRecordShowing;
import com.mmlab.m1.game.module.AnswerSending;
import com.mmlab.m1.game.module.Chest;
import com.mmlab.m1.game.module.GameData;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.POI;
import com.mmlab.m1.game.module.UrlMediaPlayer;
import com.mmlab.m1.game.module.chestMedia;
import com.mmlab.m1.game.thread.ChestThread;
import com.mmlab.m1.mini.MyApplication;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import static android.graphics.BitmapFactory.decodeFile;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MapFragement extends Fragment
        implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener,
        OnChestReadyCallback {

    //google map
    private MapView mapView;
    private GoogleMap googleMap;

    //google location
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //check first time
    boolean isfirstMove = true;

    //store marker
    List<Marker> treasuresMarkers = new ArrayList<Marker>();

    //store ans history
    List<AnswerRecord> alreadyAnswerRecords = new ArrayList<AnswerRecord>();

    //bottom text view
    TextView point;
    TextView score;
    TextView userName;

    //store point
    int userCoin = 0;

    //usage marker
    LatLng useLatLng;

    //fragment controller
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MapFragement mapFragement;
    private AnswerRecordFragement answerRecordFragement;
    private GroupChoiceFragement groupChoiceFragement;
    private RoomChoiceFragement roomChoiceFragement;


    //menu flpating button
    FloatingActionButton main_fab;
    boolean fab_in_map = true;

    //db handler
    private DbHandler dbHandler;
    private SQLiteDatabase db;

    //answer question  button
    private Dialog answerChestDialog;
    private FloatingActionButton soundButton;
    private Button aButton;
    private Button bButton;
    private Button cButton;
    private Button dButton;
    private Button noButton;
    private Button yesButton;
    private ImageView imgView;
    private VideoView videoView;
    private Button closeDialog;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private static UrlMediaPlayer urlMediaPlayer;
    private RecyclerView mediaList;
    private RecyclerView.Adapter chestMediaAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //time counter text
    TextView countdown;
    ProgressDialog nDialog;

    //retrofit api controller
    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(GameActivity.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
    private static ApiService api = retrofit.create(ApiService.class);
    Call<List<POI>> PoiCall;
    Call<String> minusCall;
    Call<String> call;
    Call<List<AnswerRecordShowing>> ansShowCall;
    Call<List<chestMedia>> chestMediaCall;

    ChestThread chestThread = new ChestThread(this);

    private Uri fileUri;
    Uri selectedData;
    Button upload_media;
    EditText media_txt;
    String dataPath;

    public static CountDownTimer countDownTimer;
    int millsec;

    private int thisGameId = 0;


    //fragement oncreate
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.map, container, false);

        //db
        dbHandler = new DbHandler(getContext(), "database.db", null, 1);
        db  = dbHandler.getWritableDatabase();

        nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Get Data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        //allow thread start
        ChestThread.setDoRun(true);

        //fm initial
        fragmentManager = getActivity().getSupportFragmentManager();

        //point text
        point = (TextView) rootView.findViewById(R.id.get_point);
        point.setVisibility(TextView.INVISIBLE);

        //score text
        score = (TextView) rootView.findViewById(R.id.score);

        //set
        thisGameId = GameActivity.getGame_id();

        //main fab in main activity
        main_fab = (FloatingActionButton) rootView.findViewById(R.id.main_fab);
        main_fab.setVisibility(View.VISIBLE);
        //main_fab.setIcon(android.R.drawable.ic_menu_mylocation);
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition cameraPosition = new CameraPosition.Builder().
                        target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).
                        tilt(60).
                        zoom(19).
                        bearing(0).
                        build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        //answer record fab
        FloatingActionButton fab_1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEndGame(getActivity());

            }
        });

        //end game activity fab
        FloatingActionButton fab_2 = (FloatingActionButton) rootView.findViewById(R.id.fab_2);
        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragement switch to answer record page
                fragmentTransaction = fragmentManager.beginTransaction();
                answerRecordFragement = new AnswerRecordFragement();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_bottom,R.anim.enter_from_bottom,R.anim.exit_to_bottom);
                fragmentTransaction.add(R.id.main_fragment,answerRecordFragement);
                fragmentTransaction.addToBackStack("MapFragment");//add to stack
                fragmentTransaction.commit();


            }
        });

        //exit game activity fab
        FloatingActionButton fab_3 = (FloatingActionButton) rootView.findViewById(R.id.fab_3);
        fab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog(getActivity());


            }
        });


        //google map view
        mapView = (MapView) rootView.findViewById(R.id.map);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //setting text
        userName  = (TextView) rootView.findViewById(R.id.user);
        userName.setText(GameActivity.getUser_nm());

        //count down
        countdown = (TextView)rootView.findViewById(R.id.countdown);
        millsec = GameActivity.getGame_data().getEnd_time()*1000;
        Toast.makeText(getActivity(), "遊戲"+millsec, Toast.LENGTH_SHORT);
        if(millsec >= 0) {
            countDownTimer = new CountDownTimer(millsec, 1000) {
                @Override
                public void onFinish() {
                    try {
                        closeGame();

                        countDownTimer = null;

                    } catch (Throwable t) {
                        Log.d("QQ", "error : " + t);
                    }

                }

                @Override
                public void onTick(long millisUntilFinished) {
                    long min = (millisUntilFinished / 1000) / 60;
                    long sec = (millisUntilFinished / 1000) % 60;

                    countdown.setText("剩餘 " + min + ":" + sec);

                    //api update data
                    if(chestThread.isDoRun()){
                        apiUpdateGameData();
                    }


                }
            }.start();
        }
        else{
            closeGame();
        }


        return rootView;
    }

    //fragement stop
    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when fragement is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    //when map ready
    @Override
    public void onMapReady(GoogleMap Gmap) {
        googleMap = Gmap;
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
        else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

        //map setting
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        //googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //custom marker listener
        googleMap.setOnMarkerClickListener(this);

    }

    //google api request
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //google api connect callback
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    //google api connect callback
    @Override
    public void onConnectionSuspended(int i) {}

    //google api connect callback
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    //lacation change callback
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        //when first in
        if(isfirstMove) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            GameActivity.setLat(location.getLatitude());
            GameActivity.setLng(location.getLongitude());
            isfirstMove = false;
            //move cerma to current location
            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(latLng).
                    tilt(60).
                    zoom(19).
                    bearing(0).
                    build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            //call api to place group poi
            apifGetPoi();
            chestThread.startMarkUpdateThread(GameActivity.getGame_id(),GameActivity.getUser_id());
            apiAnswerShowing(GameActivity.getUser_id(),GameActivity.getGame_id());
            nDialog.dismiss();
            forceUpdateMarker();
        }
        else{
            forceUpdateMarker();
        }

    }

    //when location perimission is not allow
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //call back when chest update
    @Override
    public void onChestDataComplete(final List<Chest> dataChestList){
        //update ui
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {


                googleMap.clear();
                apifGetPoi();

                for(int i=0;i<dataChestList.size();i++){
                    Chest thisChest = dataChestList.get(i);

                    boolean show = true;
                    double distance = 0;

                    if(thisChest.getDistance() > 0) {
                        LatLng targetLatLng = new LatLng(thisChest.getLat(), thisChest.getLng());
                        distance = SphericalUtil.computeDistanceBetween(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), targetLatLng);
                        if (distance > thisChest.getDistance()) {
                            show = false;
                        }

                    }

                    if(thisChest.getNum() > 0){
                        if (thisChest.getRemain() <= 0) {
                            show = false;
                        }
                    }
                    Log.d("OAO", "chest no." + i + " chest distance : (" + distance + ", " +  thisChest.getDistance()+"), " + " chest number : (" + thisChest.getRemain() + ", " +  thisChest.getNum() +") = " + show);
                    LatLng tempLatLng = new LatLng(thisChest.getLat(), thisChest.getLng());
                    Marker tempMarker = googleMap.addMarker(new MarkerOptions()
                            .position(tempLatLng)
                            .icon((BitmapDescriptorFactory.fromResource(R.mipmap.the_chest))));
                    tempMarker.setTag(thisChest);
                    tempMarker.setVisible(show);
                }



            }
        });
    }

    @Override
    public void onNoChestData() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                googleMap.clear();
                apifGetPoi();

            }
        });

    }

    //event for marker click
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (!marker.equals(mCurrLocationMarker) && marker.getTag()!=null) {
            Chest t = (Chest)marker.getTag();
            Log.d("OAO", "pick chest id : " + t.getId());
            int temp = t.getQuestion_type();
            //True or false dialog
            if(temp == 1){
                showQuestionTFDialog(getActivity(), marker);
            }
            //muti-cjoice dialog
            else if(temp==2){
                showQuestionChoiceDialog(getActivity(), marker);
            }
            else if(temp==3){
                GameActivity.setPick_chest_text(t.getQuestion());
                GameActivity.setPick_chest_id(t.getId());
                FragementMultiMediaQuestion fragementMultiMediaQuestion = new FragementMultiMediaQuestion();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_fragment, fragementMultiMediaQuestion);
                fragmentTransaction.addToBackStack("MapFragment");
                fragmentTransaction.commit();
            }
            else if(temp==4){
                showQuestionChoiceDialog(getActivity(), marker);
            }
            else if(temp==5){
                showVentureEndingDialog(getActivity(), marker);
            }
            //unknow type
            else{
                Toast.makeText(getActivity(), "功能未開放" + t.getQuestion(), Toast.LENGTH_LONG).show();
            }

        }
        return false;
    }

    public void showQuestionTFDialog(Activity activity, final Marker marker) {
        final Chest chest = (Chest)marker.getTag();

        answerChestDialog = new Dialog(activity);
        answerChestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        answerChestDialog.setContentView(R.layout.question_tf);
        answerChestDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressBar = (ProgressBar) answerChestDialog.findViewById(R.id.progress_loader);

        TextView text = (TextView) answerChestDialog.findViewById(R.id.question_context);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(chest.getQuestion());

        soundButton = (FloatingActionButton)answerChestDialog.findViewById(R.id.sound_player);

        mediaList = (RecyclerView) answerChestDialog.findViewById(R.id.media_list);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mediaList.setLayoutManager(mLayoutManager);
        apiChestMedia(chest.getId(),soundButton);

        closeDialog = (Button) answerChestDialog.findViewById(R.id.close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlMediaPlayer!=null){
                    urlMediaPlayer.release();
                }
                ChestMediaAdapter.closeAudio();
                answerChestDialog.dismiss();
            }
        });

        yesButton = (Button) answerChestDialog.findViewById(R.id.btn_yes);
        yesButton.setText("是");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCheckAnswer(chest,"T",answerChestDialog);
            }
        });

        noButton = (Button) answerChestDialog.findViewById(R.id.btn_no);
        noButton.setText("否");
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCheckAnswer(chest,"F",answerChestDialog);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(answerChestDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        answerChestDialog.getWindow().setAttributes(lp);


        answerChestDialog.show();

    }

    public void showQuestionChoiceDialog(Activity activity, final Marker marker) {
        final Chest chest = (Chest)marker.getTag();

        answerChestDialog = new Dialog(activity);
        answerChestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        if(chest.getOption1() == null && chest.getOption2() == null && chest.getOption3() == null && chest.getOption4() == null ){
            answerChestDialog.setContentView(R.layout.question_1_choice);
        }
        else if(chest.getOption1() != null && chest.getOption2() == null && chest.getOption3() == null && chest.getOption4() == null ){
            answerChestDialog.setContentView(R.layout.question_1_choice);
        }
        else if(chest.getOption1() != null && chest.getOption2() != null && chest.getOption3() == null && chest.getOption4() == null ){
            answerChestDialog.setContentView(R.layout.question_2_choice);
        }
        else if(chest.getOption1() != null && chest.getOption2() != null && chest.getOption3() != null && chest.getOption4() == null ){
            answerChestDialog.setContentView(R.layout.question_3_choice);
        }
        else{
            answerChestDialog.setContentView(R.layout.question_4_choice);
        }

        answerChestDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressBar = (ProgressBar) answerChestDialog.findViewById(R.id.progress_loader);

        TextView text = (TextView) answerChestDialog.findViewById(R.id.question_context);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(chest.getQuestion());

        soundButton = (FloatingActionButton)answerChestDialog.findViewById(R.id.sound_player);

        mediaList = (RecyclerView) answerChestDialog.findViewById(R.id.media_list);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mediaList.setLayoutManager(mLayoutManager);
        apiChestMedia(chest.getId(),soundButton);

        closeDialog = (Button) answerChestDialog.findViewById(R.id.close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlMediaPlayer!=null){
                    urlMediaPlayer.release();
                }
                ChestMediaAdapter.closeAudio();
                answerChestDialog.dismiss();
            }
        });

        if(chest.getOption1()!=null){
            aButton = (Button) answerChestDialog.findViewById(R.id.btn_a);
            aButton.setText("(A)"+chest.getOption1());
            aButton.setMovementMethod(new ScrollingMovementMethod());
            aButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(chest.getQuestion_type()){
                        case 1:
                            apiCheckAnswer(chest,"A",answerChestDialog);
                            break;
                        case 2:
                            apiCheckAnswer(chest,"A",answerChestDialog);
                            break;
                        case 3:
                            apiCheckAnswer(chest,"A",answerChestDialog);
                            break;
                        case 4:
                            closeDialog();
                            showOneButtnDialog(chest.getHint1());
                            break;
                    }

                }
            });

        }

        if(chest.getOption2()!=null) {
            bButton = (Button) answerChestDialog.findViewById(R.id.btn_b);
            bButton.setText("(B)"+chest.getOption2());
            bButton.setMovementMethod(new ScrollingMovementMethod());
            bButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(chest.getQuestion_type()){
                        case 1:
                            apiCheckAnswer(chest,"B",answerChestDialog);
                            break;
                        case 2:
                            apiCheckAnswer(chest,"B",answerChestDialog);
                            break;
                        case 3:
                            apiCheckAnswer(chest,"B",answerChestDialog);
                            break;
                        case 4:
                            closeDialog();
                            showOneButtnDialog(chest.getHint2());
                            break;
                    }
                }
            });
        }

        if(chest.getOption3()!=null) {
            cButton = (Button) answerChestDialog.findViewById(R.id.btn_c);
            cButton.setText("(C)"+chest.getOption3());
            cButton.setMovementMethod(new ScrollingMovementMethod());
            cButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(chest.getQuestion_type()){
                        case 1:
                            apiCheckAnswer(chest,"C",answerChestDialog);
                            break;
                        case 2:
                            apiCheckAnswer(chest,"C",answerChestDialog);
                            break;
                        case 3:
                            apiCheckAnswer(chest,"C",answerChestDialog);
                            break;
                        case 4:
                            closeDialog();
                            showOneButtnDialog(chest.getHint3());
                            break;
                    }
                }
            });
        }

        if(chest.getOption4()!=null) {
            dButton = (Button) answerChestDialog.findViewById(R.id.btn_d);
            dButton.setText("(D)"+chest.getOption4());
            dButton.setMovementMethod(new ScrollingMovementMethod());
            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(chest.getQuestion_type()){
                        case 1:
                            apiCheckAnswer(chest,"D",answerChestDialog);
                            break;
                        case 2:
                            apiCheckAnswer(chest,"D",answerChestDialog);
                            break;
                        case 3:
                            apiCheckAnswer(chest,"D",answerChestDialog);
                            break;
                        case 4:
                            closeDialog();
                            showOneButtnDialog(chest.getHint4());
                            break;
                    }
                }
            });
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(answerChestDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        answerChestDialog.show();
        answerChestDialog.getWindow().setAttributes(lp);

        answerChestDialog.show();

    }

    public void showVentureEndingDialog(Activity activity, final Marker marker) {
        final Chest chest = (Chest)marker.getTag();

        answerChestDialog = new Dialog(activity);
        answerChestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        answerChestDialog.setContentView(R.layout.question_1_choice);
        answerChestDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        progressBar = (ProgressBar) answerChestDialog.findViewById(R.id.progress_loader);

        TextView text = (TextView) answerChestDialog.findViewById(R.id.question_context);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(chest.getQuestion());

        soundButton = (FloatingActionButton)answerChestDialog.findViewById(R.id.sound_player);

        mediaList = (RecyclerView) answerChestDialog.findViewById(R.id.media_list);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mediaList.setLayoutManager(mLayoutManager);
        apiChestMedia(chest.getId(),soundButton);

        closeDialog = (Button) answerChestDialog.findViewById(R.id.close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlMediaPlayer!=null){
                    urlMediaPlayer.release();
                }
                ChestMediaAdapter.closeAudio();
                answerChestDialog.dismiss();
            }
        });


        aButton = (Button) answerChestDialog.findViewById(R.id.btn_a);
        aButton.setText("抵達終點（上傳記錄）");
        aButton.setMovementMethod(new ScrollingMovementMethod());
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            closeDialog();
            showOneButtnDialog("恭喜到終點");
            AnswerRecord ac = new AnswerRecord(GameActivity.getUser_id(),null,1,chest.getId(),GameActivity.getGame_id(),mLastLocation.getLatitude(),mLastLocation.getLongitude(),chest.getPoint());
            apiAnswerRecord(ac,chest.getPoint());
            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(answerChestDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        answerChestDialog.getWindow().setAttributes(lp);

        answerChestDialog.show();
    }

    //alert dialog for logout
    public void showAlertDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_2btn);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView titleText = (TextView) dialog.findViewById(R.id.title_text);
        titleText.setText("do you want exit?");

        Button leftButton = (Button) dialog.findViewById(R.id.left_btn);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
                ChestThread.setDoRun(false);
                closeDialog();
                fragmentTransaction = fragmentManager.beginTransaction();
                groupChoiceFragement = new GroupChoiceFragement();
                fragmentTransaction.replace(R.id.main_fragment,groupChoiceFragement);
                fragmentTransaction.commit();
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

    public void apifGetPoi(){
        PoiCall = api.getPOI(new Id(0,GameActivity.getGroup_id(),0,0,0,0));
        PoiCall.enqueue(new Callback<List<POI>>() {
            @Override
            public void onResponse(Call<List<POI>> call, Response<List<POI>> response) {
                List<POI> pois = response.body();
                if(pois != null) {
                    for (int i = 0; i < pois.size(); i++) {
                        POI temp = pois.get(i);
                        LatLng tempPoiLatLng = new LatLng(temp.getLatitude(), temp.getLongitude());
                        Marker tempPoi = googleMap.addMarker(new MarkerOptions().position(tempPoiLatLng));
                        tempPoi.setTitle(temp.getPOI_title());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<POI>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void apiCheckAnswer(final Chest chest,final String user_answer,final Dialog dialog){
        minusCall = api.minusChest(new AnswerSending(chest.getId(),user_answer,GameActivity.getUser_id(),GameActivity.getGame_id()));
        minusCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d("msg", chest.getId() + " result :"+ response.body());

                if(response.body().equals("chest is empty")){
                    //alert
                    Toast.makeText(getActivity(), "您太晚回答，已經被其他人搶走了", Toast.LENGTH_LONG).show();
                    closeDialog();
                    forceUpdateMarker();

                }
                else if (response.body().equals("answer is right")){

                    int chestPoint = chest.getPoint();

                    //record answer to server
                    AnswerRecord ac = new AnswerRecord(GameActivity.getUser_id(),user_answer,1,chest.getId(),GameActivity.getGame_id(),mLastLocation.getLatitude(),mLastLocation.getLongitude(),chest.getPoint());
                    apiAnswerRecord(ac,chestPoint);

                }
                else if (response.body().equals("answer is wrong")){

                    int chestPoint = chest.getPoint();

                    //record answer to server
                    AnswerRecord ac = new AnswerRecord(GameActivity.getUser_id(),user_answer,0,chest.getId(),GameActivity.getGame_id(),mLastLocation.getLatitude(),mLastLocation.getLongitude(),chest.getPoint());
                    apiAnswerRecord(ac,chestPoint);
                }
                else if (response.body().equals("already answer")){

                    Toast.makeText(getActivity(), "您已經回答過了....", Toast.LENGTH_LONG).show();
                    closeDialog();
                    forceUpdateMarker();
                }
                else{
                    //need retry
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void apiAnswerRecord(final AnswerRecord answerRecord,final int chestPoint){
        call = api.ansRecord(answerRecord);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("msg", " update the answer :" + response);

                if (response.body().equals("already answer") || response.body().toString() == "already answer"){
                    Toast.makeText(getActivity(), "您已經回答過了....", Toast.LENGTH_LONG).show();
                    closeDialog();
                    forceUpdateMarker();
                }
                else if(response.body().equals("finish") || response.body().toString() == "finish"){
                    if(answerRecord.getCorrectness() == 1){
                        //alert write
                        Toast.makeText(getActivity(), "恭喜，您答對了！！！", Toast.LENGTH_LONG).show();
                        closeDialog();

                        if(chestPoint > 0){
                            //count coin animation
                            userCoin = userCoin + chestPoint;
                            point.setText("+" + chestPoint );
                            point.setVisibility(TextView.VISIBLE);
                            point.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.get_point));
                            point.setVisibility(TextView.INVISIBLE);
                            score.setText("分數:" + userCoin);
                        }

                        forceUpdateMarker();

                    }
                    else{
                        //alert wrong
                        Toast.makeText(getActivity(), "好可惜，您答錯了～", Toast.LENGTH_LONG).show();
                        closeDialog();
                        forceUpdateMarker();

                    }
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void apiAnswerShowing(final int user_id,final int game_id){
        ansShowCall = api.getAnsShowData(new Id(user_id,0,0,game_id,0,0));
        ansShowCall.enqueue(new Callback<List<AnswerRecordShowing>>() {
            @Override
            public void onResponse(Call<List<AnswerRecordShowing>> call, Response<List<AnswerRecordShowing>> response) {
                Log.d("msg", " get user answer history : " + response);
                if(response.body()!=null){
                    int temp = 0;
                    for(int i = 0;i < response.body().size();i++){
                        if(response.body().get(i).getCorrectness() == 1)
                            temp = temp + response.body().get(i).getPoint();
                    }
                    userCoin = temp;
                    if(score != null) {
                        score.setText("分數:" + userCoin);
                    }

                    Log.d("msg", "count user point history : " + userCoin);
                }
                else{
                    Log.d("msg", "user has not answer yet");
                }

            }

            @Override
            public void onFailure(Call<List<AnswerRecordShowing>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void apiChestMedia( int chest_id ,final FloatingActionButton soundButton){
        chestMediaCall = api.getChestMedia(new Id(0,0,0,0,chest_id,0));
        chestMediaCall.enqueue(new Callback<List<chestMedia>>() {
            @Override
            public void onResponse(Call<List<chestMedia>> call, final Response<List<chestMedia>> response) {
                Log.d("OAO", "get chest media : " + response);
                if(response.body()!=null){
                    Log.d("OAO", "chest media size : " + response.body().size());

                    for(int i =0;i<response.body().size();i++){
                        if(response.body().get(i).getATT_format().equals("expound")){
                            final String expound_url = response.body().get(i).getATT_url();
                            soundButton.setVisibility(View.VISIBLE);
                            soundButton.setOnClickListener(new View.OnClickListener() {
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
                    Log.d("OAO", "chest media size : " + response.body().size());
                    chestMediaAdapter = new ChestMediaAdapter(response.body(), getActivity());
                    if(mediaList != null)
                        mediaList.setAdapter(chestMediaAdapter);
                    if(progressBar!=null)
                        progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Log.d("OAO", " chest media has no media");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<chestMedia>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void forceUpdateMarker(){
        Call<List<Chest>> call2 = api.getTreasure(new Id(GameActivity.getUser_id(),0,0,GameActivity.getGame_id(),0,0));
        call2.enqueue(new Callback<List<Chest>>() {
            @Override
            public void onResponse(Call<List<Chest>> call2, Response<List<Chest>> response) {

                googleMap.clear();
                apifGetPoi();

                if(response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Chest thisChest = response.body().get(i);
                        boolean show = true;
                        double distance = 0;
                        if(thisChest.getDistance() > 0) {
                            LatLng targetLatLng = new LatLng(thisChest.getLat(), thisChest.getLng());
                            distance = SphericalUtil.computeDistanceBetween(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), targetLatLng);

                            if (distance > thisChest.getDistance()) {
                                show = false;
                            }
                        }
                        if (thisChest.getNum() > 0) {
                            if (thisChest.getRemain() <= 0) {
                                show = false;
                            }
                        }
                        Log.d("OAO", "chest no." + i + " chest distance : (" + distance + ", " +  thisChest.getDistance()+"), " + " chest number : (" + thisChest.getRemain() + ", " +  thisChest.getNum() +") = " + show);

                        LatLng tempLatLng = new LatLng(thisChest.getLat(), thisChest.getLng());
                        Marker tempMarker = googleMap.addMarker(new MarkerOptions()
                                .position(tempLatLng)
                                .icon((BitmapDescriptorFactory.fromResource(R.mipmap.the_chest))));
                        tempMarker.setTag(thisChest);
                        tempMarker.setVisible(show);
                        nDialog.dismiss();
                        //Log.d("msg", "add : " + tempMarker.getPosition());

                    }

                }
                else{
                    Log.d("msg", "force null complete");
                }
            }
            @Override
            public void onFailure(Call<List<Chest>> call, Throwable t) {
                Log.d("Error01", t.getMessage());
            }
        });
    }

    public void palyAudio(String url){
        try {
            closeAudio();
            ChestMediaAdapter.closeAudio();
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

    public void closeDialog(){
        if(urlMediaPlayer!=null){
            urlMediaPlayer.release();
        }
        if(answerChestDialog != null){
            answerChestDialog.dismiss();
        }

    }

    private void showOneButtnDialog(String title){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_1btn);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title_text = (TextView) dialog.findViewById(R.id.title_text);
        title_text.setText(title);
        Button bottom_btn = (Button) dialog.findViewById(R.id.bottom_btn);
        bottom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showEndGame(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_2btn);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView titleText = (TextView) dialog.findViewById(R.id.title_text);
        titleText.setText("是否強制結束遊戲");

        Button leftButton = (Button) dialog.findViewById(R.id.left_btn);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(GameActivity.getGroup_leader_id() == GameActivity.getUser_id()) {
                    apiEndGame();
                }
                else{
                    Toast.makeText(getActivity(), "只有leader可以執行此決定", Toast.LENGTH_LONG).show();
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

    private void apiEndGame(){
        Call<String> callEndGame = api.endGame(new Id(0,0,0,GameActivity.getGame_id(),0,0));
        callEndGame.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> callEndGame, Response<String> response) {
                Log.d("msg", "end game "+response.body());

                closeGame();

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error01", t.getMessage());
            }
        });
    }

    private void closeGame(){
        closeDialog();
        ChestThread.setDoRun(false);


        for (int i = fragmentManager.getBackStackEntryCount() - 1; i > 0; i--) {
            Log.d("frag", fragmentManager.getBackStackEntryAt(i).getName());
            if (!fragmentManager.getBackStackEntryAt(i).getName().equalsIgnoreCase("GroupFragment")) {
                fragmentManager.popBackStack();
            }
            else
            {
                break;
            }
        }

        if(thisGameId == GameActivity.getGame_id()){
//            Toast.makeText(getActivity(), "遊戲結束～", Toast.LENGTH_SHORT).show();
            showOneButtnDialog("遊戲已結束~");
        }


    }

    public static void apiUpdateGameData(){
        Call<List<GameData>> call3 = api.getGameData(new Id(0,0,0,GameActivity.getGame_id(),0,0));
        call3.enqueue(new Callback<List<GameData>>() {
            @Override
            public void onResponse(Call<List<GameData>> call3, Response<List<GameData>> response) {
                Log.d("msg", "get end time : "+response.body().get(0).getEnd_time());
                GameActivity.setGame_data(response.body().get(0));

                if(response.body().get(0).getEnd_time() <= 0){
                    if(countDownTimer != null){
                        countDownTimer.onFinish();
                    }
                }

            }
            @Override
            public void onFailure(Call<List<GameData>> call, Throwable t) {
                Log.d("Error01", t.getMessage());
            }
        });
    }





}
