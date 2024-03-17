package com.mamits.citymatic.ui.navigator.fragment;


import com.google.gson.JsonObject;
import com.mamits.citymatic.ui.navigator.base.BaseNavigator;

public interface CartFragmentNavigator extends BaseNavigator {


    void showProgressBars();

    void checkInternetConnection(String message);

    void hideProgressBars();

    void checkValidation(int errorCode, String message);

    void throwable(Throwable throwable);

    void onSuccessCartItems(JsonObject jsonObject);

    void onSuccessCartItemRemoved(JsonObject jsonObject);

    void onSuccessCartItemUpdated(JsonObject jsonObject);
}
