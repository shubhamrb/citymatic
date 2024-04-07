package com.mamits.citymatic.di.module.fragment;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.fragment.PostReviewViewModel;
import com.mamits.citymatic.viewmodel.fragment.ReviewViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class PostReviewModule {

    @Provides
    public PostReviewViewModel providesPostReviewViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new PostReviewViewModel(mDataManger, mSchedulerProvider);
    }

}
