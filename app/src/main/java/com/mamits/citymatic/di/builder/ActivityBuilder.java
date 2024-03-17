package com.mamits.citymatic.di.builder;

import com.mamits.citymatic.di.module.activity.AddAddressActivityModule;
import com.mamits.citymatic.di.module.activity.DashboardActivityModule;
import com.mamits.citymatic.di.module.activity.ForgotPasswordActivityModule;
import com.mamits.citymatic.di.module.activity.LoginActivityModule;
import com.mamits.citymatic.di.module.activity.MessageModule;
import com.mamits.citymatic.di.module.activity.OtpActivityModule;
import com.mamits.citymatic.di.module.activity.PaymentActivityModule;
import com.mamits.citymatic.di.module.activity.RegisterActivityModule;
import com.mamits.citymatic.di.module.activity.ResetPasswordModule;
import com.mamits.citymatic.di.module.activity.WebViewActivityModule;
import com.mamits.citymatic.di.module.fragment.AddressModule;
import com.mamits.citymatic.di.module.fragment.AllSubcategoryFragmentModule;
import com.mamits.citymatic.di.module.fragment.CartFragmentModule;
import com.mamits.citymatic.di.module.fragment.ChangePasswordModule;
import com.mamits.citymatic.di.module.fragment.CouponsFragmentModule;
import com.mamits.citymatic.di.module.fragment.HistoryFragmentModule;
import com.mamits.citymatic.di.module.fragment.HomeFragmentModule;
import com.mamits.citymatic.di.module.fragment.NotificationFragmentModule;
import com.mamits.citymatic.di.module.fragment.OrderDetailsModule;
import com.mamits.citymatic.di.module.fragment.OrderSummaryModule;
import com.mamits.citymatic.di.module.fragment.ProfileFragmentModule;
import com.mamits.citymatic.di.module.fragment.ScheduleFragmentModule;
import com.mamits.citymatic.di.module.fragment.StoreDetailFragmentModule;
import com.mamits.citymatic.di.scope.ActivityScope;
import com.mamits.citymatic.di.scope.FragmentScope;
import com.mamits.citymatic.ui.activity.AddAddressActivity;
import com.mamits.citymatic.ui.activity.CouponsActivity;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.activity.ForgotPasswordActivity;
import com.mamits.citymatic.ui.activity.LoginActivity;
import com.mamits.citymatic.ui.activity.MessageActivity;
import com.mamits.citymatic.ui.activity.OtpActivity;
import com.mamits.citymatic.ui.activity.PaymentActivity;
import com.mamits.citymatic.ui.activity.RegisterActivity;
import com.mamits.citymatic.ui.activity.ResetPasswordActivity;
import com.mamits.citymatic.ui.activity.UpdateAddressActivity;
import com.mamits.citymatic.ui.activity.WebViewActivity;
import com.mamits.citymatic.ui.fragment.AddressFragment;
import com.mamits.citymatic.ui.fragment.AllSubcategoryFragment;
import com.mamits.citymatic.ui.fragment.CartFragment;
import com.mamits.citymatic.ui.fragment.ChangePasswordFragment;
import com.mamits.citymatic.ui.fragment.HistoryFragment;
import com.mamits.citymatic.ui.fragment.HomeFragment;
import com.mamits.citymatic.ui.fragment.NotificationFragment;
import com.mamits.citymatic.ui.fragment.OrderDetailsFragment;
import com.mamits.citymatic.ui.fragment.OrderSummaryFragment;
import com.mamits.citymatic.ui.fragment.ProfileFragment;
import com.mamits.citymatic.ui.fragment.ScheduleBookingFragment;
import com.mamits.citymatic.ui.fragment.StoreDetailFragment;
import com.mamits.citymatic.ui.fragment.UpdateProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    @ActivityScope
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = {RegisterActivityModule.class})
    @ActivityScope
    abstract RegisterActivity bindRegisterActivity();

    @ContributesAndroidInjector(modules = {ForgotPasswordActivityModule.class})
    @ActivityScope
    abstract ForgotPasswordActivity bindForgotActivity();

    @ContributesAndroidInjector(modules = {OtpActivityModule.class})
    @ActivityScope
    abstract OtpActivity bindOtpActivity();

    @ContributesAndroidInjector(modules = {DashboardActivityModule.class})
    @ActivityScope
    abstract DashboardActivity bindDashboardActivity();

    @ContributesAndroidInjector(modules = {WebViewActivityModule.class})
    @ActivityScope
    abstract WebViewActivity bindWebViewActivity();

    @ContributesAndroidInjector(modules = {HomeFragmentModule.class})
    @FragmentScope
    abstract HomeFragment bindHomeFragment();

    @ContributesAndroidInjector(modules = {HistoryFragmentModule.class})
    @FragmentScope
    abstract HistoryFragment bindHistoryFragment();

    @ContributesAndroidInjector(modules = {AllSubcategoryFragmentModule.class})
    @FragmentScope
    abstract AllSubcategoryFragment bindAllSubcategoryFragment();

    @ContributesAndroidInjector(modules = {OrderDetailsModule.class})
    @FragmentScope
    abstract OrderDetailsFragment bindOrderDetailsFragment();

    @ContributesAndroidInjector(modules = {StoreDetailFragmentModule.class})
    @FragmentScope
    abstract StoreDetailFragment bindStoreDetailFragment();

    @ContributesAndroidInjector(modules = {MessageModule.class})
    @ActivityScope
    abstract MessageActivity bindMessageActivity();

    @ContributesAndroidInjector(modules = {NotificationFragmentModule.class})
    @FragmentScope
    abstract NotificationFragment bindNotificationFragment();

    @ContributesAndroidInjector(modules = {ProfileFragmentModule.class})
    @FragmentScope
    abstract ProfileFragment bindProfileFragment();

    @ContributesAndroidInjector(modules = {PaymentActivityModule.class})
    @ActivityScope
    abstract PaymentActivity bindPaymentActivity();

    @ContributesAndroidInjector(modules = {ChangePasswordModule.class})
    @FragmentScope
    abstract ChangePasswordFragment bindChangePasswordFragment();

    @ContributesAndroidInjector(modules = {ResetPasswordModule.class})
    @ActivityScope
    abstract ResetPasswordActivity bindResetPasswordActivity();

    @ContributesAndroidInjector(modules = {ProfileFragmentModule.class})
    @FragmentScope
    abstract UpdateProfileFragment bindUpdateProfileFragment();

    @ContributesAndroidInjector(modules = {CouponsFragmentModule.class})
    @ActivityScope
    abstract CouponsActivity bindCouponsActivity();

    @ContributesAndroidInjector(modules = {CartFragmentModule.class})
    @FragmentScope
    abstract CartFragment bindCartFragment();

    @ContributesAndroidInjector(modules = {ScheduleFragmentModule.class})
    @FragmentScope
    abstract ScheduleBookingFragment bindScheduleBookingFragment();

    @ContributesAndroidInjector(modules = {AddAddressActivityModule.class})
    @ActivityScope
    abstract AddAddressActivity bindAddAddressActivity();

    @ContributesAndroidInjector(modules = {AddAddressActivityModule.class})
    @ActivityScope
    abstract UpdateAddressActivity bindUpdateAddressActivity();

    @ContributesAndroidInjector(modules = {OrderSummaryModule.class})
    @FragmentScope
    abstract OrderSummaryFragment bindOrderSummaryFragment();

    @ContributesAndroidInjector(modules = {AddressModule.class})
    @FragmentScope
    abstract AddressFragment bindAddressFragment();

}
