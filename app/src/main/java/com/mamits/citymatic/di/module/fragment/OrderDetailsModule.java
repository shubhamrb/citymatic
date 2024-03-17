package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.OrderDetailsViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderDetailsModule {

    @Provides
    public OrderDetailsViewModel providesOrderDetailsViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new OrderDetailsViewModel(mDataManger, mSchedulerProvider);
    }

}
