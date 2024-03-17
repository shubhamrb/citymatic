package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.ChangePasswordViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePasswordModule {

    @Provides
    public ChangePasswordViewModel providesChangePasswordViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new ChangePasswordViewModel(mDataManger, mSchedulerProvider);
    }

}
