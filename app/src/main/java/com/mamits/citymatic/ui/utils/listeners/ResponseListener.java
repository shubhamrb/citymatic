package com.mamits.citymatic.ui.utils.listeners;


import com.google.gson.JsonObject;

public interface ResponseListener {

    void  onSuccess(JsonObject jsonObject);

    void onFailed(Throwable t);
}
