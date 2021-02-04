package com.ondi.android_ondi.View.Menu.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel> test_productList = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        setRecyclerView();
        setEditListener();
        return mainView;
    }

    private void setEditListener() {
        ((EditText) mainView.findViewById(R.id.input_search)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   //검색 api 인자값 전달 후 화면 전환
                }
               return false;
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter adapter = new ProductAdapter(getContext(),test_productList);
        recyclerView.setAdapter(adapter);
    }

    private void test_insertData() {
        test_productList.add(new ProductModel("상품이름","100000",false));
        test_productList.add(new ProductModel("상품이름","200000",true));
        test_productList.add(new ProductModel("상품이름","300000",false));
        test_productList.add(new ProductModel("상품이름","400000",false));
        test_productList.add(new ProductModel("상품이름","500000",true));
    }
}