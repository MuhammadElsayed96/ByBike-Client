package com.muhammadelsayed.bybike.activity.model;

import java.io.File;

public class UserProfile {
    private String api_token;

    private File photo;


    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }
}
