package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.FragmentProfileBinding;
import com.mamits.citymatic.ui.activity.LoginActivity;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.ProfileFragmentNavigator;
import com.mamits.citymatic.ui.notification.NotificationService;
import com.mamits.citymatic.viewmodel.fragment.ProfileFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel> implements ProfileFragmentNavigator, View.OnClickListener {

    private String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;

    @Inject
    ProfileFragmentViewModel mViewModel;
    private Context mContext;

    @Override
    public ProfileFragmentViewModel getMyViewModel() {
        return mViewModel;
    }

    @Override
    protected void initView(View view, boolean isRefresh) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);
        if (getActivity() != null) {
            mContext = getActivity();
        } else if (getBaseActivity() != null) {
            mContext = getBaseActivity();
        } else if (view.getContext() != null) {
            mContext = view.getContext();
        }
        setData();
        if (isRefresh) {
            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnEditProfile);
            new ClickShrinkEffect(binding.btnBookings);
            new ClickShrinkEffect(binding.btnAddress);
            new ClickShrinkEffect(binding.btnLogout);

            binding.btnBack.setOnClickListener(view1 -> {
                Navigation.findNavController(view).popBackStack();
            });
            binding.btnLogout.setOnClickListener(view1 -> {
                new Handler().postDelayed(() -> {
                    try {
                        mContext.stopService(new Intent(mContext, NotificationService.class));
                        mViewModel.getmDataManger().clearAllPreference();
                        startActivity(new Intent(mContext, LoginActivity.class));
                        ((Activity) mContext).finishAffinity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 200);
            });
            binding.btnEditProfile.setOnClickListener(view1 -> {
                Navigation.findNavController(view).navigate(R.id.nav_update_profile);
            });

            binding.btnBookings.setOnClickListener(view1 -> {
                Navigation.findNavController(view).navigate(R.id.nav_history);
            });
            binding.btnAddress.setOnClickListener(view1 -> {
                Navigation.findNavController(view).navigate(R.id.nav_address);
            });
        }
    }

    private void setData() {
        binding.txtName.setText(mViewModel.getmDataManger().getUsername());
        binding.txtNumber.setText(mViewModel.getmDataManger().getUserNumber() + "\n" + mViewModel.getmDataManger().getUserEmail());
    }

    @Override
    public int getBindingVariable() {
        return BR.profileView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void showProgressBars() {
        showsLoading();
    }

    @Override
    public void checkInternetConnection(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBars() {
        hidesLoading();
    }

    @Override
    public void checkValidation(int errorCode, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void throwable(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onSuccessUpdateProfile(JsonObject jsonObject) {

    }

}