package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.LoginActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @Provides
    public LoginActivityViewModel providesLoginActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new LoginActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
