package com.ondi.android_ondi.View.Payment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.ProductDetail.ProductDetailActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setToolbar(); //커스텀 툴바 적용
        setViewData();
        setClickListener();
    }

    private void setViewData() {
        //상품 이름, 가격 ,이미지 set

        ImageView img_product = findViewById(R.id.img_product_payment);
        MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this).load(R.drawable.test).apply(RequestOptions.bitmapTransform(multiOption)).into(img_product);
    }

    private void setClickListener() {
        LinearLayout btn_credit = findViewById(R.id.btn_payment_credit_card);
        btn_credit.setOnClickListener(new ClickListener());
        LinearLayout btn_kakao = findViewById(R.id.btn_payment_kakao);
        btn_credit.setOnClickListener(new ClickListener());
        LinearLayout btn_phone = findViewById(R.id.btn_payment_phone);
        btn_credit.setOnClickListener(new ClickListener());
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_payment_credit_card :{
                }
                case R.id.btn_payment_kakao :{
                }
                case R.id.btn_payment_phone:{
                }
            }
        }
    }
}