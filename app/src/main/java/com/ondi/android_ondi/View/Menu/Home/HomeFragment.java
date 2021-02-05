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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.ProductAdapter;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel.Product> productList;
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
        getData();
    }

    private void getData() {
        try {

            Call<ArrayList<ProductModel.Product>> call = RetrofitClient.getApiService().getMainList();
            call.enqueue(new retrofit2.Callback<ArrayList<ProductModel.Product>>() {
                @Override
                public void onResponse(Call<ArrayList<ProductModel.Product>> call, Response<ArrayList<ProductModel.Product>> response) {
                    productList = (ArrayList<ProductModel.Product>) response.body();
                    setRecyclerView();
                }

                @Override
                public void onFailure(Call<ArrayList<ProductModel.Product>> call, Throwable t) {

                    t.printStackTrace();
                }

            });
        }
        catch(IllegalStateException | JsonSyntaxException exception)
        {
            System.out.println("에러 : ");
            exception.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        setEditListener();
        return mainView;
    }

    private void setEditListener() {
        ((EditText) mainView.findViewById(R.id.input_search)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   //검색 api 인자값 전달 후 화면 refresh
                    Call<List<ProductModel.Product>> call = RetrofitClient.getApiService().getSearchList(textView.getText().toString());
                    call.enqueue(new retrofit2.Callback<List<ProductModel.Product>>() {
                        @Override
                        public void onResponse(Call<List<ProductModel.Product>> call, Response<List<ProductModel.Product>> response) {
                            productList = (ArrayList<ProductModel.Product>) response.body();
                            setRecyclerView();
                        }

                        @Override
                        public void onFailure(Call<List<ProductModel.Product>> call, Throwable t) {
                            Toast.makeText(getContext(),"검색 실패",Toast.LENGTH_SHORT);
                            t.printStackTrace();
                        }

                    });

                }
               return false;
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }

}