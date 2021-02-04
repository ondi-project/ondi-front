package com.ondi.android_ondi.View.Menu.ProductDetail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.Dialog.AuctionDialog;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Login.SignInFragment;
import com.ondi.android_ondi.View.Payment.PaymentActivity;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    Context context;
    ViewPager2 viewPager;
    LinearLayout layoutIndicator;
    ArrayList<String> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        test_insertData();

        initView();
        setToolbar(); //커스텀 툴바 적용
        setViewPager();
        setClickListener();
    }

    private void initView() {
        context = this;
        ImageView img_seller = findViewById(R.id.img_seller_detail);
        Glide.with(this).load(R.drawable.test_user).circleCrop().into(img_seller);

        Switch btn_live = findViewById(R.id.btn_live);
        btn_live.setOnCheckedChangeListener(new CheckChangeListener());
    }

    private void setClickListener() {
        LinearLayout btn_payment = findViewById(R.id.btn_payment);
        btn_payment.setOnClickListener(new ClickListener(this));
        LinearLayout btn_chat = findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new ClickListener(this));

    }

    private void setViewPager() {
        viewPager = findViewById(R.id.viewpager_product_detail);
        layoutIndicator = findViewById(R.id.layout_indicators);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new ViewPagerAdapter(this, imgList));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(imgList.size());
    }

    private void test_insertData() {
        imgList.add("https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg");
        imgList.add("https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg");
        imgList.add("https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg");
        imgList.add("https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg");
        imgList.add("https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.custom_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.custom_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.custom_indicator_inactive));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class ClickListener implements View.OnClickListener {
        Context context;
        public ClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_payment :{
                    //결제하기 페이지
                    Intent intent = new Intent(context,PaymentActivity.class);
                    startActivity(intent);
                }
                case R.id.btn_chat :{
                    //거래하기 페이지 (채팅)
                }
            }
        }
    }

    public class CheckChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
            if(is_checked){
                //off->on
                AuctionDialog auctionDialog = new AuctionDialog(context);
                auctionDialog.show();
            }
            else{
                //on->off
            }
        }
    }
}