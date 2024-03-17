package com.mamits.citymatic.data.model.orders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailDataModel implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("category_id")
    String category_id;

    @SerializedName("sub_category_id")
    int sub_category_id;

    @SerializedName("image")
    String image;

    @SerializedName("service_type")
    int service_type;

    @SerializedName("quantity")
    int quantity;

    @SerializedName("price")
    String price;

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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
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
}
