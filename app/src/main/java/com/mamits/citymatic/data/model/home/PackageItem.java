package com.mamits.citymatic.data.model.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackageItem implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("product_id")
    String product_id;

    @SerializedName("type")
    int type;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("price")
    String price;

    @SerializedName("status")
    int status;

    @SerializedName("isDefault")
    int isDefault;

    @SerializedName("created_by")
    int created_by;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("city_id")
    int city_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", status=" + status +
                ", isDefault=" + isDefault +
                ", created_by=" + created_by +
                ", created_at='" + created_at + '\'' +
                ", city_id=" + city_id +
                '}';
    }
}
