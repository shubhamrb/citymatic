package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.CouponsActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class CouponsFragmentModule {

    @Provides
    public CouponsActivityViewModel providesOfferViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new CouponsActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
