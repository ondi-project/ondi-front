package com.ondi.android_ondi.API.Data;

import java.io.File;
import java.io.Serializable;


public class PostProduct implements Serializable {
    public PostProduct(String p_category, String p_name, int p_price, String p_content, File p_image, String p_tag, boolean p_nego, int p_seller) {
        this.p_category = p_category;
        this.p_name = p_name;
        this.p_price = p_price;
        this.p_content = p_content;
        this.p_image = p_image;
        this.p_tag = p_tag;
        this.p_nego = p_nego;
        this.p_seller = p_seller;
    }
    //null값 없이

    String p_category;
    String p_name;
    int p_price;
    String p_content;
    File p_image; //file
    String p_tag; // /로구분
    boolean p_nego;
    int p_seller;


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

    public int getP_price() {
        return p_price;
    }

    public void setP_price(int p_price) {
        this.p_price = p_price;
    }

    public String getP_content() {
        return p_content;
    }

    public void setP_content(String p_content) {
        this.p_content = p_content;
    }

    public File getP_image() {
        return p_image;
    }

    public void setP_image(File p_image) {
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

    public int getP_seller() {
        return p_seller;
    }

    public void setP_seller(int p_seller) {
        this.p_seller = p_seller;
    }
}
