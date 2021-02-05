package com.ondi.android_ondi.View.Menu.MyPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ondi.android_ondi.API.Data.PostLogin;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;
import com.ondi.android_ondi.View.Menu.Register.RegisterFragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class MyPageFragment extends Fragment {
    View mainView;
    AuthModel.User user;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(String param1, String param2) {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData() {
        user = AuthModel.getInstance().user;
//        Call<AuthModel> call = RetrofitClient.getApiService().getUserInfo();
//        call.enqueue(new retrofit2.Callback<AuthModel>() {
//            @Override
//            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
//                if(response.isSuccessful()){
//                    user = response.body().user;
//                    initView();
//                }
//                else{
//                    if (response.code() != 200) {
//                        try {
//                            Log.v("Error code",response.errorBody().string()+ " "+response.errorBody().contentType());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AuthModel> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_my_page, container, false);
        getData();
        initView();
        return mainView;
    }

    private void initView() {

        LinearLayout btn_edit_profile = mainView.findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(new ClickListener());

        RelativeLayout btn_sale_history = mainView.findViewById(R.id.btn_sale_history);
        RelativeLayout btn_purchase_history = mainView.findViewById(R.id.btn_purchase_history);
        RelativeLayout btn_like = mainView.findViewById(R.id.btn_like);
        RelativeLayout btn_review = mainView.findViewById(R.id.btn_review);

        TextView text_user_name = mainView.findViewById(R.id.text_user_name);
        text_user_name.setText(user.getUsername());
        TextView text_user_email = mainView.findViewById(R.id.text_user_email);
        text_user_email.setText(user.getEmail());

        btn_sale_history.setOnClickListener(new ClickListener());
        btn_purchase_history.setOnClickListener(new ClickListener());
        btn_like.setOnClickListener(new ClickListener());
        btn_review.setOnClickListener(new ClickListener());

    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_upload_photo :{
                    //todo 여기서하는게아니라 프로필 편집화면에서 하는게 맞는 것 같음.
                    break;
                }
                case R.id.btn_sale_history:{
                    ((MainActivity)getActivity()).goToSaleHistory();
                    break;
                }
                case R.id.btn_purchase_history:{
                    ((MainActivity)getActivity()).goToPurchaseHistory();
                    break;
                }
                case R.id.btn_like:{
                    ((MainActivity)getActivity()).goToLike();
                    break;
                }
                case R.id.btn_review:{
                    ((MainActivity)getActivity()).goToReview();
                    break;
                }
            }
        }
    }

}