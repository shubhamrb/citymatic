package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.AllSubcategoryFragmentViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class AllSubcategoryFragmentModule {

    @Provides
    public AllSubcategoryFragmentViewModel providesAllSubcategoryViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new AllSubcategoryFragmentViewModel(mDataManger, mSchedulerProvider);
    }

}
