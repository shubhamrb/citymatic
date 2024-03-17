package com.mamits.citymatic.data.model.booking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SlotDataModel implements Serializable {
    @SerializedName("id")
    int id;

    @SerializedName("from_time")
    String from_time;

    @SerializedName("to_time")
    String to_time;

    @SerializedName("max_order")
    int max_order;

    @SerializedName("is_active")
    int is_active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public int getMax_order() {
        return max_order;
    }

    public void setMax_order(int max_order) {
        this.max_order = max_order;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", from_time='" + from_time + '\'' +
                ", to_time='" + to_time + '\'' +
                ", max_order=" + max_order +
                ", is_active=" + is_active +
                '}';
    }
}
