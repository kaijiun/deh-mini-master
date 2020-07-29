package com.mmlab.m1.mini.service;

import com.mmlab.m1.game.module.LoginForm;
import com.mmlab.m1.game.module.User;
import com.mmlab.m1.mini.model.CountClickRequestObj;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface retrofitAPI {

    @POST("api/v1/poi_count_click")
    Call<ResponseBody> getCountClick(@Body CountClickRequestObj countClickRequestObj);
}
