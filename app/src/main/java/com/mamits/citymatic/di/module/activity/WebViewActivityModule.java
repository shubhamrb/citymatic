package com.mamits.citymatic.di.module.activity;


import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.WebViewActivityViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class WebViewActivityModule {

    @Provides
    public WebViewActivityViewModel providesWebViewActivityViewModel(IDataManager mDataManger, ISchedulerProvider mSchedulerProvider) {
        return new WebViewActivityViewModel(mDataManger, mSchedulerProvider);
    }

}
