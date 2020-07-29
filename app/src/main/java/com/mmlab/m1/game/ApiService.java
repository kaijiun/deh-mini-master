package com.mmlab.m1.game;



import com.mmlab.m1.game.module.AnswerRecord;
import com.mmlab.m1.game.module.AnswerRecordShowing;
import com.mmlab.m1.game.module.AnswerSending;
import com.mmlab.m1.game.module.Chest;
import com.mmlab.m1.game.module.GameData;
import com.mmlab.m1.game.module.GameId;
import com.mmlab.m1.game.module.Group;
import com.mmlab.m1.game.module.GroupSearch;
import com.mmlab.m1.game.module.HistoryGame;
import com.mmlab.m1.game.module.Id;
import com.mmlab.m1.game.module.LoginForm;
import com.mmlab.m1.game.module.MemberPoint;
import com.mmlab.m1.game.module.POI;
import com.mmlab.m1.game.module.Room;
import com.mmlab.m1.game.module.User;
import com.mmlab.m1.game.module.chestMedia;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    //game
    @POST("api/v1/users/login")
    Call<User> getUser(@Body LoginForm loginForm);

    @POST("api/v1/group_pois")
    Call<List<POI>> getPOI(@Body Id id);

    @POST("api/v1/get_group_list")
    Call<List<Group>> getGroup(@Body GroupSearch groupSearch);

    @POST("api/v1/get_room_list")
    Call<List<Room>> getRoomList(@Body Id id);

    @POST("api/v1/get_game_id")
    Call<List<GameId>> getGameId(@Body Id id);

    @POST("api/v1/get_game_data")
    Call<List<GameData>> getGameData(@Body Id id);

    @POST("api/v1/get_chest_list")
    Call <List<Chest>> getTreasure(@Body Id id);

    @POST("api/v1/chest_minus")
    Call<String> minusChest(@Body AnswerSending answerSending);

    @POST("api/v1/insert_answer")
    Call<String> ansRecord(@Body AnswerRecord answerRecord);

    @POST("api/v1/start_game")
    Call<ResponseBody> startGame(@Body Id id);

    @POST("api/v1/get_user_answer_record")
    Call <List<AnswerRecordShowing>> getAnsShowData(@Body Id id);

    @POST("api/v1/get_chest_media")
    Call <List<chestMedia>> getChestMedia(@Body Id id);

    @POST("api/v1/get_game_history")
    Call <List<HistoryGame>> getGameHistory(@Body Id id);

    @POST("api/v1/get_menber_point")
    Call <List<MemberPoint>> getMemberPoint(@Body Id id);

    @POST("api/v1/end_game")
    Call <String> endGame(@Body Id id);

    @Multipart
    @POST("/api/v1/upload_media_answer")
    Call<ResponseBody> uploadMediaAnswer(@Part MultipartBody.Part file01,
                                         @Part MultipartBody.Part file02,
                                         @Part MultipartBody.Part file03,
                                         @Part MultipartBody.Part file04,
                                         @Part MultipartBody.Part file05,
                                         @Part("txt") RequestBody txt,
                                         @Part("chest_id") RequestBody chest_id,
                                         @Part("user_id") RequestBody user_id,
                                         @Part("game_id") RequestBody game_id,
                                         @Part("lat") RequestBody lat,
                                         @Part("lng") RequestBody lng,
                                         @Part("point") RequestBody point

    );


}
