package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.OrderSummaryViewModel;
import com.mamits.citymatic.viewmodel.fragment.ScheduleFragmentViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderSummaryModule {

    @Provides
    public OrderSummaryViewModel providesOrderSummaryViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new OrderSummaryViewModel(mDataManger, mSchedulerProvider);
    }

}
