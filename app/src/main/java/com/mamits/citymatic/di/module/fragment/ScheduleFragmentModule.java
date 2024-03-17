package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.ScheduleFragmentViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleFragmentModule {

    @Provides
    public ScheduleFragmentViewModel providesScheduleViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new ScheduleFragmentViewModel(mDataManger, mSchedulerProvider);
    }

}
