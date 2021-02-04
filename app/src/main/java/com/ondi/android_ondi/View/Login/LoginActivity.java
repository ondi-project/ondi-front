package com.ondi.android_ondi.View.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.ondi.android_ondi.R;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SignInFragment signInFragment = new SignInFragment();
    private SignUpFragment signUpFragment = new SignUpFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFragmentManager();
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

}