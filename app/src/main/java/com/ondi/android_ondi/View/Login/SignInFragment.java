package com.ondi.android_ondi.View.Login;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ondi.android_ondi.R;

public class SignInFragment extends Fragment {
    View mainView;
    LinearLayout btn_sign_in;
    TextView btn_sign_up;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setClickListener();
        return mainView;
    }

    private void setClickListener() {
        btn_sign_up = mainView.findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new ClickListener());

        btn_sign_in = mainView.findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(new ClickListener());
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_sign_up :{
                //회원가입 페이지로 이동

                }
                case R.id.btn_sign_in :{
                //로그인 성공시 메인화면으로 이동
                }
            }
        }
    }
}