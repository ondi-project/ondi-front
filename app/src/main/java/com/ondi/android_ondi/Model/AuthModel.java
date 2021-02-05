package com.ondi.android_ondi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthModel {

    //싱글톤 객체
    private static AuthModel instance = new AuthModel();
    private AuthModel() {}
    public static AuthModel getInstance() {
        return instance;
    }

    public String access_token;
    public String refresh_token;
    @SerializedName("user")
    public User user;

    public class User{

        int id;
        String username;
        String first_name;
        String last_name;
        String email;
        List<String> groups;
        List<String> user_permissions;
        boolean is_staff;
        boolean is_active;
        boolean is_superuser;
        @Expose
        String  date_joined;
        @Expose
        String last_login;
        String phone;
        @Expose
        String image;

        public User(int id,String username, String first_name, String last_name, String email, List<String> groups, List<String> user_permissions, boolean is_staff, boolean is_active, boolean is_superuser, String date_joined, String last_login, String phone, String image) {
            this.id = id;
            this.username = username;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.groups = groups;
            this.user_permissions = user_permissions;
            this.is_staff = is_staff;
            this.is_active = is_active;
            this.is_superuser = is_superuser;
            this.date_joined = date_joined;
            this.last_login = last_login;
            this.phone = phone;
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getGroups() {
            return groups;
        }

        public void setGroups(List<String> groups) {
            this.groups = groups;
        }

        public List<String> getUser_permissions() {
            return user_permissions;
        }

        public void setUser_permissions(List<String> user_permissions) {
            this.user_permissions = user_permissions;
        }

        public boolean isIs_staff() {
            return is_staff;
        }

        public void setIs_staff(boolean is_staff) {
            this.is_staff = is_staff;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }

        public boolean isIs_superuser() {
            return is_superuser;
        }

        public void setIs_superuser(boolean is_superuser) {
            this.is_superuser = is_superuser;
        }

        public String getDate_joined() {
            return date_joined;
        }

        public void setDate_joined(String date_joined) {
            this.date_joined = date_joined;
        }

        public String getLast_login() {
            return last_login;
        }

        public void setLast_login(String last_login) {
            this.last_login = last_login;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    //"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjEyNTA4MTE0LCJqdGkiOiI5MTMxZDNlNDU4ZmU0MzJkYTllNzgzMGEwNWRhYjc4MyIsInVzZXJfaWQiOjJ9.u5RMY9-ECD5iuk02omekFQuTK8o1JwffrK39HQEnbsA",
    //    "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTYxMjU5NDIxNCwianRpIjoiNjhjNzIzYzhlNWVjNDYwNmFlNzkxMDgzN2NlYmZlMGUiLCJ1c2VyX2lkIjoyfQ.t4qANAR4EH-h0HwJmS6tV6Nxh9X0co_z0-F-5q8nTFo",
    //    "user": {
    //        "id": 2,
    //        "username": "pury",
    //        "first_name": "",
    //        "last_name": "",
    //        "email": "tndus130@naver.com",
    //        "groups": [],
    //        "user_permissions": [],
    //        "is_staff": false,
    //        "is_active": true,
    //        "is_superuser": false,
    //        "last_login": "2021-02-05T06:50:14.543990Z",
    //        "date_joined": "2021-02-05T06:50:14.325906Z",
    //        "phone": "01045443837",
    //        "image": null
}
