package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.OtpActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class OtpActivityModule {

    @Provides
    public OtpActivityViewModel providesOtpActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new OtpActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
