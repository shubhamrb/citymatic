package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.ResetPasswordViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ResetPasswordModule {

    @Provides
    public ResetPasswordViewModel providesResetViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new ResetPasswordViewModel(mDataManger, mSchedulerProvider);
    }

}
