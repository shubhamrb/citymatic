package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.PaymentActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class PaymentActivityModule {

    @Provides
    public PaymentActivityViewModel providesPaymentActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider){
    return  new PaymentActivityViewModel(mDataManger,mSchedulerProvider);
    }

}
