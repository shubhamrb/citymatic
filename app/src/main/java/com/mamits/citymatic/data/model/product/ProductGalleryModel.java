package com.mamits.citymatic.data.model.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductGalleryModel implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("image")
    String image;

    @SerializedName("image_type")
    String image_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", image_type='" + image_type + '\'' +
                '}';
    }
}
