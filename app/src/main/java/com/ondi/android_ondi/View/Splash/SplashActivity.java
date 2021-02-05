package com.ondi.android_ondi.View.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.ondi.android_ondi.View.Login.LoginActivity;
import com.ondi.android_ondi.View.Menu.MainActivity;
import com.ondi.android_ondi.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler  = new Handler();
        final Runnable r = () -> {
            final AWSMobileClient auth = AWSMobileClient.getInstance();

            AsyncTask.execute(() -> {
                if (auth.isSignedIn()) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                finish();
            });
        };
        handler.postDelayed(r,2000);
        //2000
    }
}