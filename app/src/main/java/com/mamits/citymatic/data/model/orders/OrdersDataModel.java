package com.mamits.citymatic.data.model.orders;

import com.google.gson.annotations.SerializedName;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.data.model.home.TimerObj;
import com.mamits.citymatic.data.model.login.UserDataModel;
import com.mamits.citymatic.data.model.product.ProductDataModel;

import java.io.Serializable;
import java.util.List;

public class OrdersDataModel implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("order_id")
    long order_id;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("service_id")
    int service_id;

    @SerializedName("booking_date_time")
    String booking_date_time;

    @SerializedName("offer_id")
    int offer_id;

    @SerializedName("offer_amount")
    String offer_amount;

    @SerializedName("order_amount")
    String order_amount;

    @SerializedName("payable_amount")
    String payable_amount;

    @SerializedName("status")
    int status;

    @SerializedName("payment_status")
    int payment_status;

    @SerializedName("payment_file")
    String payment_file;

    @SerializedName("description")
    String description;

    @SerializedName("payment_type")
    String payment_type;

    @SerializedName("type")
    String type;

    @SerializedName("order_completed_by")
    int order_completed_by;

    @SerializedName("order_detail")
    List<OrderDetailDataModel> order_detail;

    @SerializedName("order_address")
    AddressDataModel order_address;

    @SerializedName("orderdatetime")
    String orderdatetime;

    @SerializedName("order_completion_time")
    int order_completion_time;

    @SerializedName("rating_status")
    int rating_status;

    @SerializedName("happy_code")
    int happy_code;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("accepted_at")
    String accepted_at;

    @SerializedName("completed_at")
    String completed_at;

    /*@SerializedName("assign_order")
    float assign_order;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getBooking_date_time() {
        return booking_date_time;
    }

    public void setBooking_date_time(String booking_date_time) {
        this.booking_date_time = booking_date_time;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_amount() {
        return offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_file() {
        return payment_file;
    }

    public void setPayment_file(String payment_file) {
        this.payment_file = payment_file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder_completed_by() {
        return order_completed_by;
    }

    public void setOrder_completed_by(int order_completed_by) {
        this.order_completed_by = order_completed_by;
    }

    public List<OrderDetailDataModel> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(List<OrderDetailDataModel> order_detail) {
        this.order_detail = order_detail;
    }

    public AddressDataModel getOrder_address() {
        return order_address;
    }

    public void setOrder_address(AddressDataModel order_address) {
        this.order_address = order_address;
    }

    public String getOrderdatetime() {
        return orderdatetime;
    }

    public void setOrderdatetime(String orderdatetime) {
        this.orderdatetime = orderdatetime;
    }

    public int getOrder_completion_time() {
        return order_completion_time;
    }

    public void setOrder_completion_time(int order_completion_time) {
        this.order_completion_time = order_completion_time;
    }

    public int getRating_status() {
        return rating_status;
    }

    public void setRating_status(int rating_status) {
        this.rating_status = rating_status;
    }

    public int getHappy_code() {
        return happy_code;
    }

    public void setHappy_code(int happy_code) {
        this.happy_code = happy_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAccepted_at() {
        return accepted_at;
    }

    public void setAccepted_at(String accepted_at) {
        this.accepted_at = accepted_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(String completed_at) {
        this.completed_at = completed_at;
    }
}
