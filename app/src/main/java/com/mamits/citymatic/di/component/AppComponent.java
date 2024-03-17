package com.mamits.citymatic.di.component;



import com.mamits.citymatic.CityMaticApplication;
import com.mamits.citymatic.di.builder.ActivityBuilder;
import com.mamits.citymatic.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {AppModule.class, AndroidInjectionModule.class, AndroidSupportInjectionModule.class, ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<CityMaticApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<CityMaticApplication> {
    }
}
