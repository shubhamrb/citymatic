package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.HomeFragmentViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragmentModule {

    @Provides
    public HomeFragmentViewModel providesHomeViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new HomeFragmentViewModel(mDataManger, mSchedulerProvider);
    }

}
