package com.mamits.citymatic.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.mamits.citymatic.ViewModelProviderFactory;
import com.mamits.citymatic.data.datamanager.IDataManager;
import com.mamits.citymatic.ui.base.BaseActivity;
import com.mamits.citymatic.ui.utils.rx.ISchedulerProvider;
import com.mamits.citymatic.viewmodel.activity.LoginActivityViewModel;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {
    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Provides
    public LoginActivityViewModel providesMainActivityViewModel(IDataManager MainActivityViewModel, ISchedulerProvider mSchedulerProvider) {
        Supplier<LoginActivityViewModel> supplier = () -> new LoginActivityViewModel(MainActivityViewModel, mSchedulerProvider);
        ViewModelProviderFactory<LoginActivityViewModel> factory = new ViewModelProviderFactory<>(LoginActivityViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginActivityViewModel.class);
    }


}
