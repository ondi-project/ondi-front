package com.ondi.android_ondi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.Model.AuctionModel;
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ItemViewHolder> {
    Context context;
    ArrayList<AuctionModel> auctionList;

    public AuctionAdapter(Context context, ArrayList<AuctionModel> list) {
        this.context = context;
        this.auctionList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_auction, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AuctionModel.Product product = auctionList.get(position).getL_product();
        Glide.with(context).load("https://c9e33e74f42d.ngrok.io" + product.getP_image())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(holder.productIv);
        holder.productName.setText(product.getP_name());
        holder.productPrice.setText(String.valueOf(product.getP_price()));
    }

    @Override
    public int getItemCount() {
        return auctionList != null? auctionList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productIv;
        TextView productName;
        TextView productPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productIv = itemView.findViewById(R.id.iv_auction_item);
            productName = itemView.findViewById(R.id.tv_auction_item_title);
            productPrice = itemView.findViewById(R.id.tv_auction_item_price);
        }
    }

}
