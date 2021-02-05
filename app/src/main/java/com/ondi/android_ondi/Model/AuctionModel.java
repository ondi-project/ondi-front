package com.ondi.android_ondi.Model;

public class AuctionModel {
    int id;
    String l_date;
    ProductModel.Product l_product;

    public AuctionModel(int id, String l_date, ProductModel.Product l_product) {
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

    public ProductModel.Product getL_product() {
        return l_product;
    }

    public void setL_product(ProductModel.Product l_product) {
        this.l_product = l_product;
    }
}
