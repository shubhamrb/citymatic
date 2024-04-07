package com.mamits.citymatic.data.model.reviews;

import android.service.autofill.UserData;

import com.google.gson.annotations.SerializedName;
import com.mamits.citymatic.data.model.login.UserDataModel;
import com.mamits.citymatic.data.model.orders.OrderDetailDataModel;

import java.io.Serializable;
import java.util.List;

public class ReviewsDataModel implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("order_id")
    int order_id;

    @SerializedName("rating")
    int rating;

    @SerializedName("review")
    String review;

    @SerializedName("file")
    String file;

    @SerializedName("file_type")
    String file_type;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("orders")
    List<OrderDetailDataModel> orders;

    @SerializedName("user_name")
    UserDataModel user_name;

    public UserDataModel getUser_name() {
        return user_name;
    }

    public void setUser_name(UserDataModel user_name) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<OrderDetailDataModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetailDataModel> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", file='" + file + '\'' +
                ", file_type='" + file_type + '\'' +
                ", created_at='" + created_at + '\'' +
                ", orders=" + orders +
                ", user_name=" + user_name +
                '}';
    }
}
