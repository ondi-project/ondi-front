package com.ondi.android_ondi.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.ondi.android_ondi.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ItemViewHolder> {
    Context context;
    ArrayList<Bitmap> bitmapList;

    public PhotoAdapter(Context context, ArrayList<Bitmap> list) {
        this.context = context;
        this.bitmapList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(bitmapList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img_photo;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
        }

        public void bind(Bitmap bitmap,Context context){
            MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context).load(bitmap).apply(RequestOptions.bitmapTransform(multiOption)).into(img_photo);
        }
    }

}
