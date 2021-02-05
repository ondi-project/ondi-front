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
import com.amazonaws.mobile.client.results.SignUpResult;
import com.ondi.android_ondi.API.Data.PostRegister;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.R;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText nameEdt, emailEdt, passwordEdt ,passwordEdt2,phoneEdt;
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
        passwordEdt2 = view.findViewById(R.id.edt_sign_up_pwd2);
        phoneEdt = view.findViewById(R.id.edt_phone_number);
        btn_sign_in = view.findViewById(R.id.btn_sign_up_sign_up);
        btn_sign_up = view.findViewById(R.id.btn_sign_up_sign_in);

        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    public void signUpButtonClick(View view) {
        String name = nameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String password2 = passwordEdt2.getText().toString();
        String phone = phoneEdt.getText().toString();

        try {
            // 회원가입
            AWSMobileClient.getInstance().signUp(email, password, new HashMap<>(), null, new com.amazonaws.mobile.client.Callback<SignUpResult>() {
                @Override
                public void onResult(SignUpResult result) {

                    Call<AuthModel> call = RetrofitClient.getApiService().postUser(new PostRegister(name,email,password,password2,phone));
                    call.enqueue(new Callback<AuthModel>() {
                        @Override
                        public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                            if(response.isSuccessful()){
                                AuthModel.getInstance().user = response.body().user;
                                runOnUiThread(() -> Toast.makeText(getContext(), "회원가입 완료", Toast.LENGTH_SHORT).show());
                                ((LoginActivity)getActivity()).goToSignIn();
                            }
                            else{
                                if (response.code() != 200) {
                                    try {
                                        Log.v("Error code 400",response.errorBody().string()+ " "+response.errorBody().contentType());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                runOnUiThread(() -> Toast.makeText(getContext(), "회원가입 실패: "+response.message(), Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

                @Override
                public void onError(final Exception e) {
                    runOnUiThread(() -> Toast.makeText(getContext(), "회원가입 실패: "+e.getMessage(), Toast.LENGTH_SHORT).show());
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