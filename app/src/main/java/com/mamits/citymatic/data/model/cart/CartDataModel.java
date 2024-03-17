package com.mamits.citymatic.data.model.cart;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartDataModel implements Serializable {
    @SerializedName("id")
    int id;

    @SerializedName("product_id")
    int product_id;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("package_id")
    int package_id;

    @SerializedName("quantity")
    int quantity;

    @SerializedName("price")
    String price;

    @SerializedName("service_type")
    int service_type;

    @SerializedName("image")
    String image;

    @SerializedName("product_name")
    String product_name;

    @SerializedName("package_name")
    String package_name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", user_id=" + user_id +
                ", package_id=" + package_id +
                ", quantity=" + quantity +
                ", price='" + price + '\'' +
                ", service_type=" + service_type +
                ", image='" + image + '\'' +
                ", product_name='" + product_name + '\'' +
                ", package_name='" + package_name + '\'' +
                '}';
    }
}
