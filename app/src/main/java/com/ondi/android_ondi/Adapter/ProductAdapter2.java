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
import com.ondi.android_ondi.View.Menu.Detail.ProductDetailActivity;

import java.util.ArrayList;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.ItemViewHolder> {
    //마이페이지 - 거래 리스트에 사용되는 adapter. fragment종류에 따라서 visibility 바꾸면 복잡할 것 같아서 새로 만듬.

    Context context;
    ArrayList<ProductModel.Product> productList;

    public ProductAdapter2(Context context, ArrayList<ProductModel.Product> list) {
        this.context = context;
        this.productList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(context).inflate(R.layout.item_product_mypage, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(productList.get(position),context);

        //상품 클릭
        ImageView img_product = holder.itemView.findViewById(R.id.img_product);
        img_product.setOnClickListener(new ClickListener(position));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_product_name;
        TextView text_product_price;
        ImageView img_product;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text_product_name = itemView.findViewById(R.id.text_product_name);
            text_product_price = itemView.findViewById(R.id.text_product_price);
            img_product = itemView.findViewById(R.id.img_product);
        }

        public void bind(ProductModel.Product product,Context context){
            MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context).load(R.drawable.test).apply(RequestOptions.bitmapTransform(multiOption)).override(120,120).into(img_product);
            text_product_name.setText(product.getP_name());
            text_product_price.setText(product.getP_price());
        }
    }

    public class ClickListener implements View.OnClickListener {
        int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_product :{
                    //상세페이지로 이동
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    context.startActivity(intent);
                    break;
                }
            }
        }
    }
}
