package com.mamits.citymatic.data.model.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDataModel implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("Phone")
    private String Phone;

    @SerializedName("email")
    private String email;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("IsVerify")
    private int IsVerify;

    @SerializedName("IsActive")
    private int IsActive;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getIsVerify() {
        return IsVerify;
    }

    public void setIsVerify(int isVerify) {
        IsVerify = isVerify;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", email='" + email + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", IsVerify=" + IsVerify +
                ", IsActive=" + IsActive +
                '}';
    }
}
