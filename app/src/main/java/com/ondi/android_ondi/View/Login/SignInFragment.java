package com.ondi.android_ondi.View.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignInResult;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class SignInFragment extends Fragment {

    EditText emailEdt, passwordEdt;
    Button btn_sign_in;
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        emailEdt = view.findViewById(R.id.edt_sing_in_email);
        passwordEdt = view.findViewById(R.id.edt_sing_in_pwd);
        btn_sign_up = view.findViewById(R.id.btn_sing_in_sign_up);
        btn_sign_in = view.findViewById(R.id.btn_sing_in_sign_in);

        btn_sign_up.setOnClickListener(new ClickListener());
        btn_sign_in.setOnClickListener(new ClickListener());
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_sing_in_sign_up :
                    //회원가입 페이지로 이동
                    ((LoginActivity)getActivity()).goToSignUp();
                    break;
                case R.id.btn_sing_in_sign_in :
                    //로그인 성공시 메인화면으로 이동
                    signInButtonClick(view);
                    break;
            }
        }
    }

    public void signInButtonClick(View view) {
        // 로그인 시도
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        AWSMobileClient.getInstance().signIn(email, password, null, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult result) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}