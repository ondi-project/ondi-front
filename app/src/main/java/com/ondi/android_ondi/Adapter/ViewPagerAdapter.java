package com.ondi.android_ondi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.R;


import java.util.ArrayList;

public class ViewPagerAdapter  extends RecyclerView.Adapter<ViewPagerAdapter.ItemViewHolder> {
    Context context;
    ArrayList<String> imageList = new ArrayList<>();

    public ViewPagerAdapter(Context context,ArrayList<String> list) {
        this.context = context;
        this.imageList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_product_viewpager, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(imageList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        String baseUrl = "https://c9e33e74f42d.ngrok.io";
        ImageView img_product;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_viewpager_product);
        }

        public void bind(String imgUrl,Context context){
            MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(30));
            Glide.with(context).load(baseUrl+imgUrl).apply(RequestOptions.bitmapTransform(multiOption)).into(img_product);
        }
    }
}
