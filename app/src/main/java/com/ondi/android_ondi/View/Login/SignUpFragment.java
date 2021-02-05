package com.ondi.android_ondi.View.Login;

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
import com.amazonaws.mobile.client.results.SignUpResult;
import com.ondi.android_ondi.R;

import java.util.HashMap;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText nameEdt, emailEdt, passwordEdt;
    Button btn_sign_in;
    TextView btn_sign_up;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        nameEdt = view.findViewById(R.id.edt_sign_up_name);
        emailEdt = view.findViewById(R.id.edt_sign_up_email);
        passwordEdt = view.findViewById(R.id.edt_sign_up_pwd);
        btn_sign_in = view.findViewById(R.id.btn_sign_up_sign_up);
        btn_sign_up = view.findViewById(R.id.btn_sign_up_sign_in);

        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    public void signUpButtonClick(View view) {
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        try {
            // 회원가입
            AWSMobileClient.getInstance().signUp(email, password, new HashMap<>(), null, new Callback<SignUpResult>() {
                @Override
                public void onResult(SignUpResult result) {
                    runOnUiThread(() -> Toast.makeText(getContext(), "회원가입 완료", Toast.LENGTH_SHORT).show());
                    ((LoginActivity)getActivity()).goToSignIn();
                }

                @Override
                public void onError(final Exception e) {
                    runOnUiThread(() -> Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up_sign_in:
                ((LoginActivity)getActivity()).goToSignIn();
                break;
            case R.id.btn_sign_up_sign_up:
                signUpButtonClick(view);
                break;
        }
    }
}