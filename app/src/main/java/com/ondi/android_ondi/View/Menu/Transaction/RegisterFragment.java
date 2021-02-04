package com.ondi.android_ondi.View.Menu.Transaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    Context context;
    View mainView;
    ArrayList<String> spinnerList = new ArrayList<>();

    private static final int REQUEST_CODE = 0;

    RelativeLayout btn_upload_photo;
    LinearLayout btn_register;

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initSpinnerData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_register, container, false);
        initView();
        return mainView;
    }

    private void initView() {
        Spinner spinner = mainView.findViewById(R.id.spinner_register);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerItemListener());

        btn_upload_photo = mainView.findViewById(R.id.btn_upload_photo);
        btn_upload_photo.setOnClickListener(new ClickListener());

        btn_register = mainView.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new ClickListener());
    }

    public void initSpinnerData() {
        spinnerList.add("카테고리를 선택하세요.");
        spinnerList.add("여성의류");
        spinnerList.add("남성의류");
        spinnerList.add("패션잡화");
        spinnerList.add("뷰티/미용");
        spinnerList.add("디지털/가전");
        spinnerList.add("스포츠/레저");
        spinnerList.add("기타");
    }

    public class SpinnerItemListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            //id로 바꾸기 더러움
            switch (adapterView.getSelectedItem().toString()){
                case "카테고리를 선택하세요.":
                    ((TextView)adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
                    break;
                case "여성의류":
                case "남성의류":
                case "패션잡화":
                case "뷰티/미용":
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void setImageView(Bitmap bitmap,int count){
        //todo 어떻게 glide로 배경까지 적용하는지 모르겠음.
        ImageView img_thumbnail = mainView.findViewById(R.id.img_thumbnail);
        Glide.with(context).load(bitmap).into(img_thumbnail);

        TextView text_photo_count = mainView.findViewById(R.id.text_photo_count);
        text_photo_count.setText(String.valueOf(count));
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_upload_photo :{
                    ((MainActivity)getActivity()).openGallery();
                    break;
                }
                case R.id.btn_register :{
                    //서버에 저장 요청  -> 갤러리 오픈때문에 그냥 버튼만 누르면 mainactivity에서 처리하는걸로
                }
            }
        }
    }


}
