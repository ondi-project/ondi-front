package com.ondi.android_ondi.View.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.ondi.android_ondi.API.Data.PostLogin;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.Utils.PreferenceManager;
import com.ondi.android_ondi.View.Login.LoginActivity;
import com.ondi.android_ondi.View.Menu.MainActivity;
import com.ondi.android_ondi.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler  = new Handler();
        final Runnable r = () -> {
            final AWSMobileClient auth = AWSMobileClient.getInstance();
            auth.signOut();
            AsyncTask.execute(() -> {
                if (auth.isSignedIn()) {
                    String name = PreferenceManager.getString(this, "name");
                    String email = PreferenceManager.getString(this, "email");
                    String password = PreferenceManager.getString(this, "password");

                    Call<AuthModel> call = RetrofitClient.getApiService().loginUser(new PostLogin(name,email,password));
                    call.enqueue(new retrofit2.Callback<AuthModel>() {
                        @Override
                        public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                            if(response.isSuccessful()){
                                AuthModel.getInstance().user = response.body().user;

                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                            else{
                                if (response.code() != 200) {
                                    try {
                                        Log.v("Error code 400",response.errorBody().string()+ " "+response.errorBody().contentType());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                runOnUiThread(() -> Toast.makeText(SplashActivity.this, "로그인 실패: "+response.message(), Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        };
        handler.postDelayed(r,2000);
        //2000
    }
}