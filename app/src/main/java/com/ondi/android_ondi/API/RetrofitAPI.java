package com.ondi.android_ondi.API;

import com.ondi.android_ondi.API.Data.PostLogin;
import com.ondi.android_ondi.API.Data.PostRegister;
import com.ondi.android_ondi.Model.AuctionModel;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.Model.ResponseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    /**AUTH API**/
    @POST("/auth/register/")
    Call<AuthModel> postUser(@Body PostRegister user);

    @POST("/auth/login/")
    Call<AuthModel> loginUser(@Body PostLogin user);

    @GET("/auth/user")
    Call<AuthModel> getUserInfo();

    /**MAIN API**/
    @GET("/main")
    Call<ArrayList<ProductModel.Product>> getMainList();

    @GET("/main/search")
    Call<List<ProductModel.Product>> getSearchList(@Query("p_search") String search);

    @POST("/main/post")
    @Multipart
    Call<ResponseModel> postProduct(@Part("p_category") RequestBody category, @Part("p_name")RequestBody name,
                                    @Part("p_price") RequestBody price, @Part("p_content")RequestBody content,
                                    @Part MultipartBody.Part p_image,@Part("p_tag")RequestBody tag,
                                    @Part("p_nego")RequestBody nego,@Part("p_seller")RequestBody seller);
    //Call<ResponseModel> postProduct(@PartMap HashMap<String,RequestBody> fields); //todo error

    @GET("/main/view_product")
    Call<ArrayList<ProductModel.Product>> getProductDetail(@Query("p_id") int p_id,@Query("u_id") int u_id);

    @GET("/main/category")
    Call<ArrayList<ProductModel.Product>> getCategoryList(@Query("p_category") String p_category,@Query("view_option") String option);

    @GET("/main/livelist")
    Call<ArrayList<AuctionModel>> getAuctionList();

    @GET("/user/users/{id}/selling")
    Call<ArrayList<ProductModel.Product>> getSellingList(@Path("id") int u_id);

    @GET("/user/users/{id}/sold")
    Call<ArrayList<ProductModel.Product>> getSoldList(@Path("id") int u_id);




}
