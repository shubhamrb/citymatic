package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.AddAddressViewModel;
import com.mamits.citymatic.viewmodel.activity.VideoRecorderViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class VideoRecorderActivityModule {

    @Provides
    public VideoRecorderViewModel providesVideoRecorderViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new VideoRecorderViewModel(mDataManger, mSchedulerProvider);
    }

}
