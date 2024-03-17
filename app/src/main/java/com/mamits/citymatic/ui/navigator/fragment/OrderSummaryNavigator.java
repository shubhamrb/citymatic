package com.mamits.citymatic.ui.navigator.fragment;


import com.google.gson.JsonObject;
import com.mamits.citymatic.ui.navigator.base.BaseNavigator;

public interface OrderSummaryNavigator extends BaseNavigator {
    void showProgressBars();

    void checkInternetConnection(String message);

    void hideProgressBars();

    void checkValidation(int errorCode, String message);

    void throwable(Throwable throwable);

    void onSuccessOrderSummary(JsonObject jsonObject);

    void onSuccessCouponApplied(JsonObject jsonObject);

    void onSuccessOrderPlaced(JsonObject jsonObject);

    void onSuccessAddresses(JsonObject jsonObject);
}
