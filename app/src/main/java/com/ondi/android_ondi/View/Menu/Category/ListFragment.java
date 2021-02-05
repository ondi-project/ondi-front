package com.ondi.android_ondi.View.Menu.Category;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel> test_productList = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        mainView = inflater.inflate(R.layout.fragment_list, container, false);
        setRecyclerView();
        return mainView;
    }

    private void test_insertData() {
        test_productList.add(new ProductModel("상품이름","100000",false));
        test_productList.add(new ProductModel("상품이름","200000",true));
        test_productList.add(new ProductModel("상품이름","300000",false));
        test_productList.add(new ProductModel("상품이름","400000",false));
        test_productList.add(new ProductModel("상품이름","500000",true));
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter adapter = new ProductAdapter(getContext(),test_productList);
        recyclerView.setAdapter(adapter);
    }
}