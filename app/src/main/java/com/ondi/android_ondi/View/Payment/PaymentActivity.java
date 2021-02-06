package com.ondi.android_ondi.View.Payment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.Dialog.PaymentDialog;
import com.ondi.android_ondi.R;

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
        Glide.with(this).load(R.drawable.ic_notebook).apply(RequestOptions.bitmapTransform(multiOption)).into(img_product);
    }

    private void setClickListener() {
        LinearLayout btn_credit = findViewById(R.id.btn_credit_card);
        btn_credit.setOnClickListener(new ClickListener(this));
        LinearLayout btn_kakao = findViewById(R.id.btn_kakao);
        btn_kakao.setOnClickListener(new ClickListener(this));
        LinearLayout btn_phone = findViewById(R.id.btn_phone);
        btn_phone.setOnClickListener(new ClickListener(this));
        LinearLayout btn_payment = findViewById(R.id.btn_payment);
        btn_payment.setOnClickListener(new ClickListener(this));

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.icon_ionic_ios_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public class ClickListener implements View.OnClickListener {
        Context context;

        public ClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_credit_card:{
                }
                case R.id.btn_kakao:{
                }
                case R.id.btn_phone:{
                }
                case R.id.btn_payment:{
                    PaymentDialog dialog = new PaymentDialog(context);
                    dialog.show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}