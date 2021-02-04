package com.ondi.android_ondi.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.ondi.android_ondi.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public String USER_NAME = "";
    public String PASSWORD = "";

    EditText emailCodeEdt;
    EditText pwCodeEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final AWSMobileClient auth = AWSMobileClient.getInstance();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (auth.isSignedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    initView();
                }
            }
        });
    }

    private void initView() {
        emailCodeEdt = findViewById(R.id.edt_email_code);
        pwCodeEdt = findViewById(R.id.edt_pw_code);
    }

    public void signUpButtonClick(View view) {
        USER_NAME = emailCodeEdt.getText().toString();
        PASSWORD = pwCodeEdt.getText().toString();
        Map<String, String> attribute = new HashMap<>();

        try {
            // 회원가입
            AWSMobileClient.getInstance().signUp(USER_NAME, PASSWORD, attribute, null, new Callback<SignUpResult>() {
                @Override
                public void onResult(SignUpResult result) {
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show());
                    Log.d("zzanzu", "회원가입 완료");
                }

                @Override
                public void onError(final Exception e) {
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show());
                    Log.d("zzanzu", "error: " + e.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signInButtonClick(View view) {
        // 로그인 시도
        USER_NAME = emailCodeEdt.getText().toString();
        PASSWORD = pwCodeEdt.getText().toString();
        AWSMobileClient.getInstance().signIn(USER_NAME, PASSWORD, null, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult result) {
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show());
                Log.d("zzanzu", "onResult: 로그인 성공" + result.toString());
                runOnUiThread(()-> Toast.makeText(SignUpActivity.this, "로그인 성공", Toast.LENGTH_LONG).show());
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show());
                Log.d("zzanzu", "onError: 로그인 실패" + e.toString());
                runOnUiThread(()-> Toast.makeText(SignUpActivity.this, "로그인 실패", Toast.LENGTH_LONG).show());
            }
        });
    }
}
