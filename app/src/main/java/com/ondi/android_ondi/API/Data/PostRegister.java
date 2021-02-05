package com.ondi.android_ondi.API.Data;

import com.google.gson.annotations.SerializedName;

public class PostRegister {

    String username;
    String email;
    String password1;
    String password2;
    String phone;

    public PostRegister(String userName, String email, String pwd, String pwd2, String phone) {
        this.username = userName;
        this.email = email;
        this.password1 = pwd;
        this.password2 = pwd2;
        this.phone = phone;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return password1;
    }

    public void setPwd(String pwd) {
        this.password1 = pwd;
    }

    public String getPwd2() {
        return password2;
    }

    public void setPwd2(String pwd2) {
        this.password2 = pwd2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
