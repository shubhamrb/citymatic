package com.mamits.citymatic.ui.utils.listeners;


public interface StringResponseListener {

    void onSuccess(String response);

    void onFailed(Throwable t);
}
