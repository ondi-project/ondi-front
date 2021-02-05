package com.ondi.android_ondi.API;

import com.ondi.android_ondi.API.Data.PostLogin;
import com.ondi.android_ondi.API.Data.PostRegister;
import com.ondi.android_ondi.Model.AuthModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface RetrofitAPI {

    //auth api

    //auth/ password/reset/ [name='rest_password_reset']
    //auth/ password/reset/confirm/ [name='rest_password_reset_confirm']
    //auth/ login/ [name='rest_login']
    //auth/ logout/ [name='rest_logout']
    //auth/ user/ [name='rest_user_details']
    //auth/ password/change/ [name='rest_password_change']
    //auth/ token/verify/ [name='token_verify']
    //auth/ token/refresh/ [name='token_refresh']
    //auth/register/

    @POST("/auth/register/")
    Call<AuthModel> postUser(@Body PostRegister user);

    @POST("/auth/login/")
    Call<AuthModel> loginUser(@Body PostLogin user);


}
