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
import com.ondi.android_ondi.Model.ReviewModel;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.Detail.ProductDetailActivity;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ItemViewHolder> {
    Context context;
    ArrayList<ReviewModel> reviewList;

    public ReviewAdapter(Context context, ArrayList<ReviewModel> list) {
        this.context = context;
        this.reviewList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(reviewList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_date;
        TextView text_review;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text_date = itemView.findViewById(R.id.text_review_date);
            text_review = itemView.findViewById(R.id.text_review);
        }

        public void bind(ReviewModel review,Context context){
            text_date.setText(review.getDate());
            text_review.setText(review.getReview());
        }
    }

}
