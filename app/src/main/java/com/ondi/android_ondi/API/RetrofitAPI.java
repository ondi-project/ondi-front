package com.ondi.android_ondi.API;

import com.ondi.android_ondi.API.Data.PostLogin;
import com.ondi.android_ondi.API.Data.PostProduct;
import com.ondi.android_ondi.API.Data.PostRegister;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.Model.ResponseModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitAPI {

    //auth api
    @POST("/auth/register/")
    Call<AuthModel> postUser(@Body PostRegister user);

    @POST("/auth/login/")
    Call<AuthModel> loginUser(@Body PostLogin user);

    //main api
    @GET("/main")
    Call<ArrayList<ProductModel.Product>> getMainList();

    @POST("/main/post/")
    @Multipart
//    Call<ResponseModel> postProduct(@Query("p_category") String category,
//                                    @Query("p_name") String name,
//                                    @Query("p_price") int price,
//                                    @Query("p_content") String content,
//                                    @Query("p_tag") String tag,
//                                    @Query("p_nego") boolean nego,
//                                    @Query("p_seller") int id,
//                                    @Part MultipartBody.Part file);
    Call<ResponseModel> postProduct(@Body PostProduct product);
    // String p_category;
    //    String p_name;
    //    int p_price;
    //    String p_content;
    //    File p_image; //file
    //    String p_tag; // /로구분
    //    boolean p_nego;
    //    int p_seller;
//    Call<ResponseModel> postProduct(@Body PostProduct product);

    //@Query("lastUID")lastUID: Int , @Query("perPage")perPage: Int


}
