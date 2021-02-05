package com.ondi.android_ondi.View.Menu.Category;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ListFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel.Product> productList = new ArrayList<>();

    RecyclerView recycler_keyword;
    RecyclerView recycler_like;
    RecyclerView recycler_view;
    RecyclerView recycler_date;

    public void setCategory(String category) {
        this.category = category;
    }
    public String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_list, container, false);
        initView();
        return mainView;
    }

    private void initView() {
        TabLayout tabs = mainView.findViewById(R.id.tabs_list);
        tabs.addTab(tabs.newTab().setText("키워드순"));
        tabs.addTab(tabs.newTab().setText("좋아요순"));
        tabs.addTab(tabs.newTab().setText("조회수순"));
        tabs.addTab(tabs.newTab().setText("날짜순"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        getData("p_keyword");

        recycler_keyword = mainView.findViewById(R.id.recycler_keyword);
        recycler_like = mainView.findViewById(R.id.recycler_like);
        recycler_view = mainView.findViewById(R.id.recycler_view);
        recycler_date = mainView.findViewById(R.id.recycler_date);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() ==0){
                    getData("p_keyword");
                    recycler_keyword.setVisibility(View.VISIBLE);
                    recycler_date.setVisibility(View.GONE);
                    recycler_like.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.GONE);
                }else if (tab.getPosition() == 1){
                    getData("p_likecount");
                    recycler_keyword.setVisibility(View.GONE);
                    recycler_date.setVisibility(View.GONE);
                    recycler_like.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                }
                else if (tab.getPosition() == 2){
                    getData("p_viewcount");
                    recycler_keyword.setVisibility(View.GONE);
                    recycler_date.setVisibility(View.GONE);
                    recycler_like.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                }
                else if (tab.getPosition() == 3){
                    getData("p_date");
                    recycler_keyword.setVisibility(View.GONE);
                    recycler_date.setVisibility(View.VISIBLE);
                    recycler_like.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { // 선택 O -> 선택
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { // 선택 O -> 선택 O
            }
        });
    }

    private void getData(String option) {
        Call<ArrayList<ProductModel.Product>> call = RetrofitClient.getApiService().getCategoryList(category,option);
        productList.clear();
        productList.removeAll(productList);
        call.enqueue(new retrofit2.Callback<ArrayList<ProductModel.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel.Product>> call, Response<ArrayList<ProductModel.Product>> response) {
                productList = (ArrayList<ProductModel.Product>) response.body();
                setRecyclerView(option);
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel.Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setRecyclerView(String option) {
        switch (option){
            case "p_keyword":{
                recycler_keyword.setLayoutManager(new GridLayoutManager(getContext(),2));
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                recycler_keyword.setAdapter(adapter);
            }
            case "p_likecount":{
                recycler_like.setLayoutManager(new GridLayoutManager(getContext(),2));
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                recycler_like.setAdapter(adapter);
            }
            case "p_viewcount":{
                recycler_view.setLayoutManager(new GridLayoutManager(getContext(),2));
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                recycler_view.setAdapter(adapter);
            }
            case "p_date":{
                recycler_date.setLayoutManager(new GridLayoutManager(getContext(),2));
                ProductAdapter adapter = new ProductAdapter(getContext(), productList);
                recycler_date.setAdapter(adapter);
            }
        }
    }
}