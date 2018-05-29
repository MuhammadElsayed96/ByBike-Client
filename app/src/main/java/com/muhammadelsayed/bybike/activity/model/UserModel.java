package com.muhammadelsayed.bybike.activity.model;

import java.io.Serializable;

public class UserModel implements Serializable{

    private String token;
    private User user;

    public UserModel(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public UserModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
