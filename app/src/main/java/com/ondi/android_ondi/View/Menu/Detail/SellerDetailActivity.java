package com.ondi.android_ondi.View.Menu.Detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Adapter.ReviewAdapter;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.Model.ReviewModel;
import com.ondi.android_ondi.Model.ScoreModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class SellerDetailActivity extends AppCompatActivity {
    ArrayList<ScoreModel> test_reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        test_insertData();
        setRecyclerView();
    }

    private void test_insertData() {
        test_reviewList.add(new ScoreModel(2,1,1,3,"좋은 물건 합리적인 가격에 가져갑니다."));
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReviewAdapter adapter = new ReviewAdapter(this,test_reviewList);
        recyclerView.setAdapter(adapter);
    }

}