package com.mamits.citymatic.viewmodel.fragment;


import android.app.Activity;

import com.androidnetworking.error.ANError;
import com.google.gson.JsonObject;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.navigator.fragment.OrderDetailsNavigator;
import com.mamits.citymatic.ui.utils.commonClasses.NetworkUtils;
import com.mamits.citymatic.ui.utils.listeners.ResponseListener;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.base.BaseViewModel;

import org.json.JSONObject;

public class OrderDetailsViewModel extends BaseViewModel<OrderDetailsNavigator> {

    public OrderDetailsViewModel(IDataManager dataManager, ISchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void fetchOrderSummary(Activity mActivity, int orderid) {
        if (NetworkUtils.isNetworkConnected(mActivity)) {
//            getmNavigator().get().showProgressBars();
            getmDataManger().fetchOrderDetails(mActivity, getmDataManger().getAccessToken(), orderid, new ResponseListener() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    try {
//                        getmNavigator().get().hideProgressBars();
                        getmNavigator().get().onSuccessOrderDetail(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    try {
//                        getmNavigator().get().hideProgressBars();
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            if (anError.getErrorBody() != null) {
                                JSONObject object = new JSONObject(anError.getErrorBody());
                                try {
                                    getmNavigator().get().checkValidation(anError.getErrorCode(), object.optString("message"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            throwable.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            getmNavigator().get().checkInternetConnection(mActivity.getResources().getString(R.string.check_internet_connection));
        }
    }
}
