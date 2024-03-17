package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.ForgotPasswordActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ForgotPasswordActivityModule {

    @Provides
    public ForgotPasswordActivityViewModel providesForgotActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new ForgotPasswordActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
