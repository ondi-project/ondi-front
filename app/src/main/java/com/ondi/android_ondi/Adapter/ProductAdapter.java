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

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ItemViewHolder> {
    Context context;
    ArrayList<ProductModel.Product> productList;

    public ProductAdapter(Context context, ArrayList<ProductModel.Product> list) {
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

        //상품 클릭
        ImageView img_product = holder.itemView.findViewById(R.id.img_product);
        img_product.setOnClickListener(new ClickListener(position));

        //좋아요 버튼 클릭
        ImageView btn_favorite = holder.itemView.findViewById(R.id.btn_like);
        btn_favorite.setOnClickListener(new ClickListener(position));

    }

    @Override
    public int getItemCount() {
        return productList != null? productList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_product_name;
        TextView text_product_price;
        ImageView img_product;
        ImageView btn_like;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text_product_name = itemView.findViewById(R.id.text_product_name);
            text_product_price = itemView.findViewById(R.id.text_product_price);
            img_product = itemView.findViewById(R.id.img_product);
            btn_like = itemView.findViewById(R.id.btn_like);
        }

        public void bind(ProductModel.Product product,Context context){
            MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(16));
            Glide.with(context).load(product.getP_image()).apply(RequestOptions.bitmapTransform(multiOption)).override(150,150).into(img_product);
            text_product_name.setText(product.getP_name());
            text_product_price.setText(product.getP_price());

            if(product.isFavorite_check()){
                //좋아요 설정 됨
                Glide.with(context).load(R.drawable.ic_baseline_favorite_24).into(btn_like);
            }
            else{
                Glide.with(context).load(R.drawable.ic_baseline_favorite_border_24).into(btn_like);
            }
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
                    intent.putExtra("p_id" ,productList.get(position).getId());
                    context.startActivity(intent);
                    break;
                }
                case R.id.btn_like:{
                    if(productList.get(position).isFavorite_check()){
                        //이미 좋아요 눌려있는 경우 -> 해제
                        productList.get(position).setFavorite_check(false);
                        //api요청
                    }
                    else{
                        //안눌려있는 경우 -> 등록
                        productList.get(position).setFavorite_check(true);
                        //api 요청
                    }
                    notifyItemChanged(position);
                    break;
                }
            }
        }
    }
}
