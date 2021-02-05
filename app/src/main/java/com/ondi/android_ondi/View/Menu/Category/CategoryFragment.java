package com.ondi.android_ondi.View.Menu.Category;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.Defined.DefinedCategory;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Login.SignInFragment;
import com.ondi.android_ondi.View.Menu.MainActivity;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    Context context;
    View mainView;

    ViewFlipper viewFlipper;
    ArrayList<Integer> imageList = new ArrayList<>();


    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test_insertData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_category, container, false);
        initView();
        return mainView;
    }

    private void initView() {
        context = getContext();

        viewFlipper = mainView.findViewById(R.id.category_view_flipper);
        for(int image : imageList){
            addFilpperImage(image);
        }

        setClickListener();

    }

    private void setClickListener() {
        LinearLayout btn_digital = mainView.findViewById(R.id.btn_digital);
        btn_digital.setOnClickListener(new ClickListener());
    }

    public void test_insertData(){
        imageList.add(R.drawable.test);
        imageList.add(R.drawable.test_user);
        imageList.add(R.drawable.test);
        imageList.add(R.drawable.test_user);
    }

    public void addFilpperImage(int image){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setBackgroundResource(image);
//        MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
//        Glide.with(context).load(image).apply(RequestOptions.bitmapTransform(multiOption)).into(imageView);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(context,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(context,android.R.anim.slide_out_right);
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_digital :{
                    ((MainActivity)getActivity()).goToCategory(DefinedCategory.getInstance().DIGITAL);
                    break;
                }
            }
        }
    }
}