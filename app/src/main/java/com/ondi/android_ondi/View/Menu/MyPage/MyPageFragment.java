package com.ondi.android_ondi.View.Menu.MyPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;
import com.ondi.android_ondi.View.Menu.Register.RegisterFragment;

public class MyPageFragment extends Fragment {
    View mainView;
    RelativeLayout btn_upload_photo;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_my_page, container, false);
        initView();
        return mainView;
    }

    private void initView() {
        btn_upload_photo = mainView.findViewById(R.id.btn_upload_photo_mypage);
        btn_upload_photo.setOnClickListener(new ClickListener());

        RelativeLayout btn_sale_history = mainView.findViewById(R.id.btn_sale_history);
        btn_sale_history.setOnClickListener(new ClickListener());
        RelativeLayout btn_purchase_history = mainView.findViewById(R.id.btn_purchase_history);
        btn_purchase_history.setOnClickListener(new ClickListener());
        RelativeLayout btn_like = mainView.findViewById(R.id.btn_like);
        btn_like.setOnClickListener(new ClickListener());
        RelativeLayout btn_review = mainView.findViewById(R.id.btn_review);
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