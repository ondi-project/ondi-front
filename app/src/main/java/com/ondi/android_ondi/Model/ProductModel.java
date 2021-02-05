package com.ondi.android_ondi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    public List<Product> productList;

    public class ProductDetail{
        @SerializedName("product")
        public ProductInfo info;
        public boolean like;
        public boolean livebutton;
    }

    public class ProductInfo{
        public Product p;
        @SerializedName("p_seller")
        public AuthModel user;
    }

    public ProductModel(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public class Product {
        int id;
        @Expose
        String p_category;
        String p_name;
        String p_price;
        @Expose
        String p_content;
        String p_image;
        @Expose
        String p_tag;
        @Expose
        boolean p_nego;
        String p_date;
        int p_viewcount;
        int p_likecount;
        @Expose
        int p_live;
        transient boolean favorite_check;

        public Product(int id, String p_name, String p_price, String p_image, String p_date, int p_viewcount, int p_likecount, boolean favorite_check) {
            this.id = id;
            this.p_name = p_name;
            this.p_price = p_price;
            this.p_image = p_image;
            this.p_date = p_date;
            this.p_viewcount = p_viewcount;
            this.p_likecount = p_likecount;
            this.favorite_check = favorite_check;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getP_price() {
            return p_price;
        }

        public void setP_price(String p_price) {
            this.p_price = p_price;
        }

        public String getP_image() {
            return p_image;
        }

        public void setP_image(String p_image) {
            this.p_image = p_image;
        }

        public String getP_date() {
            return p_date;
        }

        public void setP_date(String p_date) {
            this.p_date = p_date;
        }

        public int getP_viewcount() {
            return p_viewcount;
        }

        public void setP_viewcount(int p_viewcount) {
            this.p_viewcount = p_viewcount;
        }

        public int getP_likecount() {
            return p_likecount;
        }

        public void setP_likecount(int p_likecount) {
            this.p_likecount = p_likecount;
        }

        public boolean isFavorite_check() {
            return favorite_check;
        }

        public void setFavorite_check(boolean favorite_check) {
            this.favorite_check = favorite_check;
        }
    }

}
