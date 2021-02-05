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
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class SellerDetailActivity extends AppCompatActivity {
    ArrayList<ReviewModel> test_reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        test_insertData();
        setRecyclerView();
    }

    private void test_insertData() {
        test_reviewList.add(new ReviewModel("어제","너무 좋아용오오오옹"));
        test_reviewList.add(new ReviewModel("어제","너무 좋아용오오오옹"));
        test_reviewList.add(new ReviewModel("어제","너무 좋아용오오오옹"));
        test_reviewList.add(new ReviewModel("어제","너무 좋아용오오오옹"));
        test_reviewList.add(new ReviewModel("어제","너무 좋아용오오오옹"));
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReviewAdapter adapter = new ReviewAdapter(this,test_reviewList);
        recyclerView.setAdapter(adapter);
    }

}