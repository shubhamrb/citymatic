package com.mamits.citymatic.di.module.activity;

import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.MessageViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class MessageModule {

    @Provides
    public MessageViewModel providesMessage(IDataManager iDataManager, ISchedulerProvider iSchedulerProvider) {
        return new MessageViewModel(iDataManager, iSchedulerProvider);
    }
}
