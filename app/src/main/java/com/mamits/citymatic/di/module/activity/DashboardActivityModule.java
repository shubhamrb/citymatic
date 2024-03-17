package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.DashboardActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class DashboardActivityModule {

    @Provides
    public DashboardActivityViewModel providesDashActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new DashboardActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
