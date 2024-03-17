package com.mamits.citymatic.data.model.home;

import com.google.gson.annotations.SerializedName;
import com.mamits.citymatic.data.model.offer.OfferDataModel;
import com.mamits.citymatic.data.model.product.ProductDataModel;
import com.mamits.citymatic.data.model.store.StoreListItem;

import java.io.Serializable;
import java.util.List;

public class HomeDataModel implements Serializable {
    @SerializedName("bannerlist")
    List<BannerListItem> bannerlist;

    @SerializedName("bottom_bannerlist")
    List<BannerListItem> bottom_bannerlist;

    @SerializedName("categorylist")
    List<CategoryListItem> categorylist;

    @SerializedName("storelist")
    List<StoreListItem> storelist;

    @SerializedName("productlist")
    List<ProductDataModel> productlist;

    @SerializedName("bundleproductlist")
    List<ProductDataModel> bundleproductlist;

    @SerializedName("couponList")
    List<OfferDataModel> couponList;

    public List<BannerListItem> getBannerlist() {
        return bannerlist;
    }

    public void setBannerlist(List<BannerListItem> bannerlist) {
        this.bannerlist = bannerlist;
    }

    public List<BannerListItem> getBottom_bannerlist() {
        return bottom_bannerlist;
    }

    public void setBottom_bannerlist(List<BannerListItem> bottom_bannerlist) {
        this.bottom_bannerlist = bottom_bannerlist;
    }

    public List<CategoryListItem> getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(List<CategoryListItem> categorylist) {
        this.categorylist = categorylist;
    }

    public List<StoreListItem> getStorelist() {
        return storelist;
    }

    public void setStorelist(List<StoreListItem> storelist) {
        this.storelist = storelist;
    }

    public List<ProductDataModel> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<ProductDataModel> productlist) {
        this.productlist = productlist;
    }

    public List<ProductDataModel> getBundleproductlist() {
        return bundleproductlist;
    }

    public void setBundleproductlist(List<ProductDataModel> bundleproductlist) {
        this.bundleproductlist = bundleproductlist;
    }

    public List<OfferDataModel> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<OfferDataModel> couponList) {
        this.couponList = couponList;
    }

    @Override
    public String toString() {
        return "{" +
                "bannerlist=" + bannerlist +
                ", bottom_bannerlist=" + bottom_bannerlist +
                ", categorylist=" + categorylist +
                ", storelist=" + storelist +
                ", productlist=" + productlist +
                ", bundleproductlist=" + bundleproductlist +
                ", couponList=" + couponList +
                '}';
    }
}
