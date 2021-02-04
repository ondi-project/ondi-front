package com.ondi.android_ondi.View.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.ondi.android_ondi.R;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SignInFragment signInFragment = new SignInFragment();
    private SignUpFragment signUpFragment = new SignUpFragment();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFragmentManager();

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.login_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
    }

    private void setFragmentManager() {
        fragmentManager.beginTransaction().replace(R.id.layout_login_container,signInFragment).commitAllowingStateLoss();
    }

    public void goToSignUp(){
        fragmentManager.beginTransaction().replace(R.id.layout_login_container,signUpFragment).commitAllowingStateLoss();
    }

    public void goToSignIn(){
        fragmentManager.beginTransaction().replace(R.id.layout_login_container,signInFragment).commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}