package com.ondi.android_ondi.Model;

public class ProductModel {
    String productName;
    String productPrice;
    boolean favorite_check;

    public ProductModel(String productName, String productPrice,boolean favorite_status) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.favorite_check = favorite_status;
    }

    public boolean isFavorite_check() {
        return favorite_check;
    }

    public void setFavorite_check(boolean favorite_check) {
        this.favorite_check = favorite_check;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
