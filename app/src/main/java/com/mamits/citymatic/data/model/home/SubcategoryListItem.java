package com.mamits.citymatic.data.model.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubcategoryListItem implements Serializable {
    @SerializedName("id")
    int id;

    @SerializedName("category_id")
    String category_id;

    @SerializedName("name")
    String name;

    @SerializedName("variation")
    String variation;

    @SerializedName("image")
    String image;

    @SerializedName("description")
    String description;

    @SerializedName("product_type")
    int product_type;

    @SerializedName("packages")
    List<PackageItem> packages;

    @SerializedName("defaultPackage")
    List<PackageItem> defaultPackage;

    public List<PackageItem> getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(List<PackageItem> defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public List<PackageItem> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageItem> packages) {
        this.packages = packages;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", category_id='" + category_id + '\'' +
                ", name='" + name + '\'' +
                ", variation='" + variation + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", product_type=" + product_type +
                ", packages=" + packages +
                ", defaultPackage=" + defaultPackage +
                '}';
    }
}
