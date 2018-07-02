package com.muhammadelsayed.bybike.activity.model;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class ProfileImageUpdate {


    private String api_token;
    private MultipartBody.Part image;
    private RequestBody name;

    public ProfileImageUpdate() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public ProfileImageUpdate(String api_token, MultipartBody.Part image, RequestBody name) {
        this.api_token = api_token;
        this.image = image;
        this.name = name;
    }

    public RequestBody getName() {
        return name;
    }

    public void setName(RequestBody name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProfileImageUpdate{" +
                "api_token='" + api_token + '\'' +
                ", image=" + image +
                ", name=" + name +
                '}';
    }
}
