package com.ondi.android_ondi.View.Menu.Auction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ondi.android_ondi.API.RetrofitClient;
import com.ondi.android_ondi.Adapter.AuctionAdapter;
import com.ondi.android_ondi.Model.AuctionModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AuctionFragment extends Fragment {
    View mainView;
    ArrayList<AuctionModel> auctionList;

    public AuctionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_auction, container, false);
        return mainView;
    }

    private void getData() {
        Call<ArrayList<AuctionModel>> call = RetrofitClient.getApiService().getAuctionList();
        call.enqueue(new retrofit2.Callback<ArrayList<AuctionModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AuctionModel>> call, Response<ArrayList<AuctionModel>> response) {
                auctionList = response.body();
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<ArrayList<AuctionModel>> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = mainView.findViewById(R.id.recycler_auction);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        AuctionAdapter adapter = new AuctionAdapter(getContext(), auctionList);
        recyclerView.setAdapter(adapter);
    }
}