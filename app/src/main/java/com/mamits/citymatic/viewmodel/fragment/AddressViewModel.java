package com.mamits.citymatic.viewmodel.fragment;


import android.app.Activity;

import com.androidnetworking.error.ANError;
import com.google.gson.JsonObject;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.navigator.fragment.AddressNavigator;
import com.mamits.citymatic.ui.utils.commonClasses.NetworkUtils;
import com.mamits.citymatic.ui.utils.listeners.ResponseListener;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.base.BaseViewModel;

import org.json.JSONObject;

public class AddressViewModel extends BaseViewModel<AddressNavigator> {

    public AddressViewModel(IDataManager dataManager, ISchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void fetchAddress(Activity mActivity, String type) {
        if (NetworkUtils.isNetworkConnected(mActivity)) {
//            getmNavigator().get().showProgressBars();
            getmDataManger().fetchAddresses(mActivity, getmDataManger().getAccessToken(), type, new ResponseListener() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    try {
//                        getmNavigator().get().hideProgressBars();
                        getmNavigator().get().onSuccessAddresses(jsonObject);
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

    public void setDefaultAddress(Activity mActivity, int id) {
        if (NetworkUtils.isNetworkConnected(mActivity)) {
            getmNavigator().get().showProgressBars();
            getmDataManger().setDefaultAddress(mActivity, getmDataManger().getAccessToken(), id, new ResponseListener() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    try {
                        getmNavigator().get().hideProgressBars();
                        getmNavigator().get().onSuccessDefaultAddress(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    try {
                        getmNavigator().get().hideProgressBars();
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
