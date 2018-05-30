package com.muhammadelsayed.bybike.activity.model;

import com.muhammadelsayed.bybike.activity.utils.Utils;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String uuid;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String image;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFirstname() {
        String firstname = "";
        if (name != null)
            firstname = Utils.splitName(name)[0];
        return firstname;
    }

    public String getLastname() {
        String lastname = "";
        if (name != null && name.contains(" "))
            lastname = Utils.splitName(name)[1];
        return lastname;
    }

    public User() {}

    public User(String email, String password) {

        this.email = email;
        this.password = password;

    }

    public User(int id, String uuid, String name, String email, String password, String phone, String image, String created_at, String updated_at) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
