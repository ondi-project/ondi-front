package com.ondi.android_ondi.View.Menu.Detail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.TagAdapter;
import com.ondi.android_ondi.Adapter.ViewPagerAdapter;
import com.ondi.android_ondi.Dialog.AuctionDialog;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Chat.ChatActivity;
import com.ondi.android_ondi.View.Payment.PaymentActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    Context context;
    ViewPager2 viewPager;
    LinearLayout layoutIndicator;

    ProductModel.Product product;
    ProductModel.Product status;

    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> hashTagList = new ArrayList<>();

    String baseUrl = "http://3.34.125.92:8000";
    int p_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        p_id = getIntent().getIntExtra("p_id",0);
        getData();

        setToolbar(); //커스텀 툴바 적용
        setClickListener();
    }

    private void initView() {
        context = this;

        ImageView img_seller = findViewById(R.id.img_seller_detail);
        Glide.with(this).load(baseUrl+product.getP_name()).circleCrop().into(img_seller);

        TextView text_price_detail = findViewById(R.id.text_price_detail);
        text_price_detail.setText(product.getP_price());

        TextView text_name_detail = findViewById(R.id.text_name_detail);
        text_name_detail.setText(product.getP_name());

        TextView text_category_detail = findViewById(R.id.text_category_detail);
        text_category_detail.setText(product.getP_category());

        TextView text_views_detail = findViewById(R.id.text_views_detail);
        text_views_detail.setText(String.valueOf(product.getP_viewcount()));

        TextView text_favorite_detail = findViewById(R.id.text_favorite_detail);
        text_favorite_detail.setText(String.valueOf(product.getP_likecount()));


        checkLike();
        checkLive();
        parsingTag();

        RecyclerView recyclerView = findViewById(R.id.recycler_hash_tag);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        TagAdapter adapter = new TagAdapter(this,hashTagList);
        recyclerView.setAdapter(adapter);
    }

    private void parsingTag() {
        TextView text_content_detail = findViewById(R.id.text_content_detail);
        text_content_detail.setText(product.getP_content());
        //태그 파싱해서 list에 add
        String tags = product.getP_tag();
        String[] tagList = tags.split("/");
        for(String tag : tagList){
            hashTagList.add(tag);
        }
    }

    private void checkLive() {
        LinearLayout layout_on_air = findViewById(R.id.layout_on_air);
        if(product.getP_seller() == AuthModel.getInstance().user.getId()){
            //라이브 버튼 보이게
            layout_on_air.setVisibility(View.VISIBLE);
        }
        else{
            layout_on_air.setVisibility(View.GONE);
        }

        Switch btn_live = findViewById(R.id.btn_live);
        btn_live.setOnCheckedChangeListener(new CheckChangeListener());
        if(status.livebutton){
            //live상태
            btn_live.setChecked(true);
        }
        else{
            btn_live.setChecked(false);
        }
    }

    private void checkLike() {
        ImageView btn_favorite_detail = findViewById(R.id.btn_favorite_detail);
        if(status.like){
            Glide.with(context).load(R.drawable.ic_baseline_favorite_24).into(btn_favorite_detail);
        }
        else{
            Glide.with(context).load(R.drawable.ic_baseline_favorite_border_24).into(btn_favorite_detail);
        }
    }

    private void setClickListener() {
        LinearLayout btn_payment = findViewById(R.id.btn_payment);
        btn_payment.setOnClickListener(new ClickListener(this));
        LinearLayout btn_chat = findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new ClickListener(this));

        ImageView img_seller = findViewById(R.id.img_seller_detail);
        img_seller.setOnClickListener(new ClickListener(this));
    }

    private void setViewPager() {
        imgList.add(baseUrl+product.getP_image());
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

    private void getData() {
        Call<ArrayList<ProductModel.Product>> call = RetrofitClient.getApiService().getProductDetail(p_id,AuthModel.getInstance().user.getId());
        call.enqueue(new retrofit2.Callback<ArrayList<ProductModel.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel.Product>> call, Response<ArrayList<ProductModel.Product>> response) {
                ArrayList<ProductModel.Product> body= response.body();
                product = body.get(0);
                status = body.get(1);
                initView();
                setViewPager();
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel.Product>> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.icon_ionic_ios_arrow_back);
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
            case android.R.id.home:{
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
                    break;
                }
                case R.id.btn_chat :{
                    Intent intent = new Intent(context, ChatActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.img_seller_detail:{
                    Intent intent = new Intent(context,SellerDetailActivity.class);
                    startActivity(intent);
                    break;
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
            }
        }
    }
}