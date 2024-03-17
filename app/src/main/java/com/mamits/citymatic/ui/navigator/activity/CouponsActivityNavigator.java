package com.mamits.citymatic.ui.navigator.activity;


import com.google.gson.JsonObject;
import com.mamits.citymatic.ui.navigator.base.BaseNavigator;

public interface CouponsActivityNavigator extends BaseNavigator {


    void showProgressBars();

    void checkInternetConnection(String message);

    void hideProgressBars();

    void checkValidation(int errorCode, String message);

    void throwable(Throwable throwable);

    void onSuccessOffers(JsonObject jsonObject);
}
