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
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ondi.android_ondi.Model.ProductModel;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.ProductDetail.ProductDetailActivity;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ItemViewHolder> {
    Context context;
    ArrayList<String> tagList;

    public TagAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.tagList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_hash_tag, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(tagList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_tag;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text_tag = itemView.findViewById(R.id.text_hash_tag);
        }

        public void bind(String tag,Context context){
            text_tag.setText(tag);
        }
    }

}
