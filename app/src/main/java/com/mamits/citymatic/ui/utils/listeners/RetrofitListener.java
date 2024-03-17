package com.mamits.citymatic.ui.utils.listeners;


import com.mamits.citymatic.data.model.ErrorObject;

public interface RetrofitListener {
    void onResponseSuccess(String responseBodyString, int apiFlag);

    void onResponseError(ErrorObject errorObject, Throwable throwable, int apiFlag);
}
