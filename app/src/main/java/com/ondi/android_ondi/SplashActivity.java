package com.ondi.android_ondi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler  = new Handler();
        final Intent intent = new Intent(this,MainActivity.class);
        final Runnable r = new Runnable() {
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(r,2000);
    }
}