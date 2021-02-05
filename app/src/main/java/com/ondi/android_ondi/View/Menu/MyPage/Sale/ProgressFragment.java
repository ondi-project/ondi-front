package com.ondi.android_ondi.View.Menu.MyPage.Sale;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.ProductAdapter2;
import com.ondi.android_ondi.Model.AuthModel;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ProgressFragment extends Fragment {
    View mainView;
    ArrayList<ProductModel.Product> productList = new ArrayList<>();

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void getData() {
        Call<ArrayList<ProductModel.Product>> call = RetrofitClient.getApiService().getSellingList(AuthModel.getInstance().user.getId());
        call.enqueue(new retrofit2.Callback<ArrayList<ProductModel.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel.Product>> call, Response<ArrayList<ProductModel.Product>> response) {
                if(response.isSuccessful()){
                    productList = response.body();
                    setRecyclerView();
                }
                else{
                    if (response.code() != 200) {
                        try {
                            Log.v("Error code",response.errorBody().string()+ " "+response.errorBody().contentType());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel.Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_progress, container, false);
        return mainView;
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_list_progress);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductAdapter2 adapter = new ProductAdapter2(getContext(), productList);
        recyclerView.setAdapter(adapter);
    }
}