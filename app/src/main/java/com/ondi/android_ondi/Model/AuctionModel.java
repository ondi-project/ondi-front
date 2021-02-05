package com.ondi.android_ondi.Model;

import com.google.gson.annotations.SerializedName;

public class AuctionModel {
    @SerializedName("id")
    int id;
    @SerializedName("l_date")
    String l_date;
    @SerializedName("l_product")
    AuctionModel.Product l_product;

    public AuctionModel(int id, String l_date, AuctionModel.Product l_product) {
        this.id = id;
        this.l_date = l_date;
        this.l_product = l_product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getL_date() {
        return l_date;
    }

    public void setL_date(String l_date) {
        this.l_date = l_date;
    }

    public AuctionModel.Product getL_product() {
        return l_product;
    }

    public void setL_product(AuctionModel.Product l_product) {
        this.l_product = l_product;
    }

    public class Product {
        @SerializedName("id")
        int id;
        @SerializedName("p_name")
        String p_name;
        @SerializedName("p_price")
        int p_price;
        @SerializedName("p_image")
        String p_image;
        @SerializedName("p_date")
        String p_date;
        @SerializedName("p_likecount")
        int p_likecount;

        public Product(int id, String p_name, int p_price, String p_image, String p_date, int p_likecount) {
            this.id = id;
            this.p_name = p_name;
            this.p_price = p_price;
            this.p_image = p_image;
            this.p_date = p_date;
            this.p_likecount = p_likecount;
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

        public int getP_price() {
            return p_price;
        }

        public void setP_price(int p_price) {
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

        public int getP_likecount() {
            return p_likecount;
        }

        public void setP_likecount(int p_likecount) {
            this.p_likecount = p_likecount;
        }
    }
}
