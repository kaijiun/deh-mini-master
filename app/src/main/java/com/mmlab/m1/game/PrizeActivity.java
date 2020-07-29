package com.mmlab.m1.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.mmlab.m1.R;
import com.mmlab.m1.game.callback.GroupFragmentCallback;
import com.mmlab.m1.game.callback.OnRoomGameStatusText;
import com.mmlab.m1.game.callback.RoomAdapterCallback;
import com.mmlab.m1.game.callback.RoomFragmentCallback;
import com.mmlab.m1.game.fragement.GroupChoiceFragement;
import com.mmlab.m1.game.module.GameData;
import com.mmlab.m1.game.module.GameId;
import com.mmlab.m1.game.module.Group;
import com.mmlab.m1.game.module.GroupSearch;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.LoginForm;
import com.mmlab.m1.game.module.Room;
import com.mmlab.m1.game.module.UrlMediaPlayer;
import com.mmlab.m1.game.module.User;
import com.mmlab.m1.game.thread.ChestThread;
import com.mmlab.m1.mini.model.DEHUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrizeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GroupChoiceFragement groupChoiceFragement;

    private static UrlMediaPlayer urlMediaPlayer;
    private static VideoView videoView;
    private static MediaController mediaController;


    //api
    private static String BASE_URL = "http://deh.csie.ncku.edu.tw:8080/";
    //private static String BASE_URL = "http://140.116.82.130:8080/";
    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private static ApiService api = retrofit.create(ApiService.class);
    private static Call<List<Group>> callGroupList;
    private static Call<List<Room>> callRoomList;
    private static Call<User> callUserLogin;
    private static Call<List<GameId>> gameIdCall;
    private static Call<List<GameData>> gameDataCall;
    private static Call<ResponseBody> gameStartCall;

    //global parameter
    private static String user_pw;
    private static String user_nm;
    private static int user_id;
    private static int group_id;
    private static int group_leader_id;
    private static int game_id;
    private static int room_id;
    private static int src_id;
    private static int pick_chest_id;
    private static String pick_chest_text;
    private static double lat = 0;
    private static double lng = 0;



    private static List<Group> user_group_list  = new ArrayList<Group>();
    private static List<Room> user_room_list  = new ArrayList<Room>();
    private static GameData game_data;


    //realm db
    private Realm realm;
    private RealmResults<DEHUser> userResult;

    //fragment callback
    private static RoomFragmentCallback roomFragmentCallback;
    private static RoomAdapterCallback roomAdapterCallback;
    private static OnRoomGameStatusText onRoomGameStatusText;
    private static GroupFragmentCallback groupFragmentCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_game);

        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);

        realm = Realm.getInstance(this);
        userResult = realm.where(DEHUser.class).findAll();
        for (DEHUser user : userResult) {
            setUser_nm(user.getId());
            setUser_pw(user.getPw());
        }

        apiLogin(getUser_nm(),getUser_pw());

        ChestThread.setDoRun(false);
    }

    @Override
    public void onBackPressed() {
        if(urlMediaPlayer!=null){
            urlMediaPlayer.release();
        }
        super.onBackPressed();
    }

    public static int getGroup_leader_id() {
        return group_leader_id;
    }

    public static void setGroup_leader_id(int group_leader_id) {
        PrizeActivity.group_leader_id = group_leader_id;
    }

    public static List<Room> getUser_room_list() {
        return user_room_list;
    }

    public static void setUser_room_list(List<Room> user_room_list) {
        PrizeActivity.user_room_list = user_room_list;
    }

    public static int getUser_id() {
        return user_id;
    }

    public static void setUser_id(int i) {
        user_id = i;
    }

    public static int getGroup_id() {
        return group_id;
    }

    public static void setGroup_id(int i) {
        group_id = i;
    }

    public static int getGame_id() {
        return game_id;
    }

    public static void setGame_id(int i) {
        game_id = i;
    }

    public static String getUser_nm() {
        return user_nm;
    }

    public void setUser_nm(String s) {
        user_nm = s;
    }

    public static String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String s) {
        user_pw = s;
    }

    public static List<Group> getUser_group_list() {
        return user_group_list;
    }

    public static void setUser_group_list(List<Group> user_group_list) {
        PrizeActivity.user_group_list = user_group_list;
    }


    public static GameData getGame_data() {
        return game_data;
    }

    public static void setGame_data(GameData game_data) {
        PrizeActivity.game_data = game_data;
    }

    public static ApiService getApi() {
        return api;
    }

    public static void setApi(ApiService api) {
        PrizeActivity.api = api;
    }

    public static void buildGroupFragmentCallback(GroupFragmentCallback g){
        PrizeActivity.groupFragmentCallback = g;
    }

    public static void buildRoomFragmentCallback(RoomFragmentCallback g){
        PrizeActivity.roomFragmentCallback = g;
    }

    public static void buildRoomAdapterCallback(RoomAdapterCallback g){
        PrizeActivity.roomAdapterCallback = g;
    }

    public static void buildOnRoomGameStatusText(OnRoomGameStatusText g) {
        PrizeActivity.onRoomGameStatusText = g;
    }

    public static int getPick_chest_id() {
        return pick_chest_id;
    }

    public static void setPick_chest_id(int pick_chest_id) {
        PrizeActivity.pick_chest_id = pick_chest_id;
    }

    public static String getPick_chest_text() {
        return pick_chest_text;
    }

    public static void setPick_chest_text(String pick_chest_text) {
        PrizeActivity.pick_chest_text = pick_chest_text;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        PrizeActivity.lat = lat;
    }

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        PrizeActivity.lng = lng;
    }




    //api part ----------------------------------------------------------
    public void apiLogin(String un,String pw){
        callUserLogin = getApi().getUser(new LoginForm(un,md5(pw),"deh"));
        callUserLogin.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //login sucess
                if (response.body().getNickname()!= null) {
                    Log.d("OAO", "login sucess");
                    if(response.body()!=null) {
                        Log.d("OAO", "user_id : " + response.body().getUser_id());
                        Log.d("OAO", "user_nm : " + response.body().getNickname());

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        groupChoiceFragement = new GroupChoiceFragement();
                        fragmentTransaction.add(R.id.main_fragment, groupChoiceFragement);
                        fragmentTransaction.commit();

                        setUser_id(response.body().getUser_id());
                    }

                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("log in Error", t.getMessage());
            }
        });
    }

    public static void apiGroup(){
        callGroupList = getApi().getGroup(new GroupSearch(getUser_id(),"deh"));
        callGroupList.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call2, Response<List<Group>> response) {
                Log.d("OAO", "get grpup list sucess");
                if(response.body()!=null) {
                    Log.d("OAO", "grpup list size : " + response.body().size());
                    setUser_group_list(response.body());
                    groupFragmentCallback.onGroupListReady();
                }

            }
            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public static void apiRoom(){
        callRoomList = getApi().getRoomList(new Id(0,getGroup_id(),0,0,0,0));
        callRoomList.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call2, Response<List<Room>> response) {
                Log.d("OAO", "get room list sucess");
                if(response.body()!=null) {
                    Log.d("OAO", "rom list size : " + response.body().size());
                    setUser_room_list(response.body());
                    roomFragmentCallback.onRoomListReady();
                }
            }
            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public static void apiRoomStatus(final Button button, final int room_id,final String room_name){
        gameIdCall = api.getGameId(new Id(0,0,room_id,0,0,0));
        gameIdCall.enqueue(new Callback<List<GameId>>() {
            @Override
            public void onResponse(Call<List<GameId>> gameIdCall, Response<List<GameId>> response) {
                if(response.body() != null) {
                    if (response.body().get(0).getIs_playing() != 0){
                        setGame_id(response.body().get(0).getIs_playing());
                        roomAdapterCallback.onRoomIsGaming(button,room_id,room_name);
                    }
                    else
                        roomAdapterCallback.onRoomUnstartGame(button,room_id,room_name);
                }
                else {
                    Log.d("OAO", "grpup status = no game");
                    roomAdapterCallback.onRoomHasNoGame(button,room_id,room_name);
                }
            }
            @Override
            public void onFailure(Call<List<GameId>> gameIdCall, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public static void apiRoomStatusText(final Button button,int room_id,final String room_name){
        gameIdCall = api.getGameId(new Id(0,0,room_id,0,0,0));
        gameIdCall.enqueue(new Callback<List<GameId>>() {
            @Override
            public void onResponse(Call<List<GameId>> gameIdCall, Response<List<GameId>> response) {
                if(response.body() != null) {
                    if (response.body().get(0).getIs_playing() == 0){
                        onRoomGameStatusText.returnText(button,"未開始",room_name);
                    }
                    else if(response.body().get(0).getIs_playing() == -1){
                        onRoomGameStatusText.returnText(button,"批改中",room_name);
                    }
                    else{
                        setGame_id(response.body().get(0).getIs_playing());
                        onRoomGameStatusText.returnText(button,"進行中",room_name);
                    }

                }
                else {
                    onRoomGameStatusText.returnText(button,"尚未建立",room_name);
                }
            }
            @Override
            public void onFailure(Call<List<GameId>> gameIdCall, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public static void apiGameData(){
        gameDataCall = api.getGameData(new Id(0,0,0,getGame_id(),0,0));
        gameDataCall.enqueue(new Callback<List<GameData>>() {
            @Override
            public void onResponse(Call<List<GameData>> gameDataCall, Response<List<GameData>> response) {
                Log.d("OAO", " get game data sucess");
                if(response.body()!=null) {
                    Log.d("OAO", "going to game data id = " + response.body().get(0).getId());
                    PrizeActivity.setGame_data(response.body().get(0));
                    roomAdapterCallback.onGameDataReady();
                }
            }
            @Override
            public void onFailure(Call<List<GameData>> gameDataCall, Throwable t) {

            }
        });
    }

    public static void apiGameStart(final Button button, final int roon_id,final String room_name){

        gameStartCall = api.startGame(new Id(0,0,roon_id,0,0,0));
        gameStartCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("OAO", " start game sucess : "+ response);
                //showToast("game start",GameActivity.this);
                roomAdapterCallback.onStartRoomGame(button,room_id,room_name);
                apiRoomStatusText(button,room_id,room_name);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showToast(String a, Context context) {
        Toast ifx = Toast.makeText(context, "Please enter name in correct format.", Toast.LENGTH_SHORT);
        ifx.show();
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static int getRoom_id() {
        return room_id;
    }

    public static void setRoom_id(int room_id) {
        PrizeActivity.room_id = room_id;
    }

    public static int getSrc_id() {
        return src_id;
    }

    public static void setSrc_id(int src_id) {
        PrizeActivity.src_id = src_id;
    }

    public static Context getAppContext() {
        return PrizeActivity.getAppContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
