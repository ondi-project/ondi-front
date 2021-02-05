package com.ondi.android_ondi.View.Menu.MyPage.Like;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Adapter.ProductAdapter2;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class LikeFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel> test_productList = new ArrayList<>();

    public LikeFragment() {
        // Required empty public constructor
    }

    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_like, container, false);
        test_insertData();
        setRecyclerView();
        return mainView;
    }

    private void test_insertData() {
        test_productList.clear();
        test_productList.removeAll(test_productList);
        test_productList.add(new ProductModel("상품이름","100000",false));
        test_productList.add(new ProductModel("상품이름","200000",true));
        test_productList.add(new ProductModel("상품이름","300000",false));
        test_productList.add(new ProductModel("상품이름","400000",false));
        test_productList.add(new ProductModel("상품이름","500000",true));
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_list_like);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter adapter = new ProductAdapter(getContext(),test_productList);
        recyclerView.setAdapter(adapter);
    }
}