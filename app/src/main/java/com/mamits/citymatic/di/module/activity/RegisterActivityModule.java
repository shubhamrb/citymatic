package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.RegisterActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterActivityModule {

    @Provides
    public RegisterActivityViewModel providesRegisterActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new RegisterActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
