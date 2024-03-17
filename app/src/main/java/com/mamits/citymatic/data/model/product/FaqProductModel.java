package com.mamits.citymatic.data.model.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FaqProductModel implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
