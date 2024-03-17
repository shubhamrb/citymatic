package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.AddAddressViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class AddAddressActivityModule {

    @Provides
    public AddAddressViewModel providesAddAddressViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new AddAddressViewModel(mDataManger, mSchedulerProvider);
    }

}
