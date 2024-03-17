package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.CartFragmentViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class CartFragmentModule {

    @Provides
    public CartFragmentViewModel providesCartViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new CartFragmentViewModel(mDataManger, mSchedulerProvider);
    }

}
