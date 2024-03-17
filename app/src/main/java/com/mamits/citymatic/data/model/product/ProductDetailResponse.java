package com.mamits.citymatic.data.model.product;

import com.google.gson.annotations.SerializedName;
import com.mamits.citymatic.data.model.home.PackageItem;

import java.io.Serializable;
import java.util.List;

public class ProductDetailResponse implements Serializable {

    @SerializedName("product")
    List<ProductDataModel> product;

    @SerializedName("packages")
    List<PackageItem> packages;

    @SerializedName("productGallery")
    List<ProductGalleryModel> productGallery;

    @SerializedName("faqs")
    List<FaqProductModel> faqs;

    @SerializedName("procedures")
    List<ProceduresProductModel> procedures;

    @SerializedName("defaultPackage")
    List<PackageItem> defaultPackage;

    public List<PackageItem> getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(List<PackageItem> defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public List<ProductDataModel> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDataModel> product) {
        this.product = product;
    }

    public List<PackageItem> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageItem> packages) {
        this.packages = packages;
    }

    public List<ProductGalleryModel> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(List<ProductGalleryModel> productGallery) {
        this.productGallery = productGallery;
    }

    public List<FaqProductModel> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqProductModel> faqs) {
        this.faqs = faqs;
    }

    public List<ProceduresProductModel> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProceduresProductModel> procedures) {
        this.procedures = procedures;
    }

    @Override
    public String toString() {
        return "{" +
                "product=" + product +
                ", packages=" + packages +
                ", productGallery=" + productGallery +
                ", faqs=" + faqs +
                ", procedures=" + procedures +
                ", defaultPackage=" + defaultPackage +
                '}';
    }
}
