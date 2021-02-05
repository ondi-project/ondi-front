package com.ondi.android_ondi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    public List<Product> productList;

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
        @Expose
        boolean p_buy;
        @Expose
        int p_seller;

        @Expose
        public boolean like;
        @Expose
        public boolean livebutton;

        transient boolean favorite_check;


        public Product(int id, String p_category, String p_name, String p_price, String p_content, String p_image, String p_tag, boolean p_nego, String p_date, int p_viewcount, int p_likecount, int p_live, boolean p_buy, int p_seller, boolean like, boolean livebutton, boolean favorite_check) {
            this.id = id;
            this.p_category = p_category;
            this.p_name = p_name;
            this.p_price = p_price;
            this.p_content = p_content;
            this.p_image = p_image;
            this.p_tag = p_tag;
            this.p_nego = p_nego;
            this.p_date = p_date;
            this.p_viewcount = p_viewcount;
            this.p_likecount = p_likecount;
            this.p_live = p_live;
            this.p_buy = p_buy;
            this.p_seller = p_seller;
            this.like = like;
            this.livebutton = livebutton;
            this.favorite_check = favorite_check;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getP_category() {
            return p_category;
        }

        public void setP_category(String p_category) {
            this.p_category = p_category;
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

        public String getP_content() {
            return p_content;
        }

        public void setP_content(String p_content) {
            this.p_content = p_content;
        }

        public String getP_image() {
            return p_image;
        }

        public void setP_image(String p_image) {
            this.p_image = p_image;
        }

        public String getP_tag() {
            return p_tag;
        }

        public void setP_tag(String p_tag) {
            this.p_tag = p_tag;
        }

        public boolean isP_nego() {
            return p_nego;
        }

        public void setP_nego(boolean p_nego) {
            this.p_nego = p_nego;
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

        public int getP_live() {
            return p_live;
        }

        public void setP_live(int p_live) {
            this.p_live = p_live;
        }

        public boolean isP_buy() {
            return p_buy;
        }

        public void setP_buy(boolean p_buy) {
            this.p_buy = p_buy;
        }

        public int getP_seller() {
            return p_seller;
        }

        public void setP_seller(int p_seller) {
            this.p_seller = p_seller;
        }

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }

        public boolean isLivebutton() {
            return livebutton;
        }

        public void setLivebutton(boolean livebutton) {
            this.livebutton = livebutton;
        }

        public boolean isFavorite_check() {
            return favorite_check;
        }

        public void setFavorite_check(boolean favorite_check) {
            this.favorite_check = favorite_check;
        }
    }

}
