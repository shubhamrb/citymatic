package com.mamits.citymatic.data.model.address;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressDataModel implements Serializable {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("email")
    String email;

    @SerializedName("house_flat")
    String house_flat;

    @SerializedName("address")
    String address;

    @SerializedName("address_1")
    String address_1;

    @SerializedName("landmark")
    String landmark;

    @SerializedName("pincode")
    String pincode;

    @SerializedName("address_type")
    String address_type;

    @SerializedName("IsDefault")
    int IsDefault;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouse_flat() {
        return house_flat;
    }

    public void setHouse_flat(String house_flat) {
        this.house_flat = house_flat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public int getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(int isDefault) {
        IsDefault = isDefault;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", house_flat='" + house_flat + '\'' +
                ", address='" + address + '\'' +
                ", address_1='" + address_1 + '\'' +
                ", landmark='" + landmark + '\'' +
                ", pincode='" + pincode + '\'' +
                ", address_type='" + address_type + '\'' +
                ", IsDefault=" + IsDefault +
                '}';
    }
}
