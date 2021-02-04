package com.ondi.android_ondi.View.Menu.Home;

import android.content.Context;
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

import java.util.ArrayList;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ItemViewHolder> {
    Context context;
    ArrayList<ProductModel> productList;

    public ProductAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.productList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(productList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_product_name;
        TextView text_product_price;
        ImageView img_product;
        ImageView btn_favorite;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text_product_name = itemView.findViewById(R.id.text_product_name);
            text_product_price = itemView.findViewById(R.id.text_product_price);
            img_product = itemView.findViewById(R.id.img_product);
            btn_favorite = itemView.findViewById(R.id.btn_favorite);
        }

        public void bind(ProductModel product,Context context){
            MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context).load(R.drawable.test).apply(RequestOptions.bitmapTransform(multiOption)).override(150,150).into(img_product);
            text_product_name.setText(product.getProductName());
            text_product_price.setText(product.getProductPrice());

            if(product.isFavorite_check()){
                //좋아요 설정 됨
                Glide.with(context).load(R.drawable.ic_baseline_favorite_24).into(btn_favorite);
            }
            else{
                Glide.with(context).load(R.drawable.ic_baseline_favorite_border_24).into(btn_favorite);
            }
        }
    }
}
