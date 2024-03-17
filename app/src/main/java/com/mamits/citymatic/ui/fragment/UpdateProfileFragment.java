package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.FragmentUpdateProfileBinding;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.ProfileFragmentNavigator;
import com.mamits.citymatic.viewmodel.fragment.ProfileFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class UpdateProfileFragment extends BaseFragment<FragmentUpdateProfileBinding, ProfileFragmentViewModel> implements ProfileFragmentNavigator, View.OnClickListener {

    private String TAG = "UpdateProfileFragment";
    private FragmentUpdateProfileBinding binding;

    @Inject
    ProfileFragmentViewModel mViewModel;
    private Context mContext;
    private Gson mGson;

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
        if (isRefresh) {
            mGson = new Gson();
            binding.etFirstName.setText(mViewModel.getmDataManger().getUsername().split(" ")[0]);
            binding.etLastName.setText(mViewModel.getmDataManger().getUsername().split(" ")[1]);
            binding.etEmail.setText(mViewModel.getmDataManger().getUserEmail());
            binding.etMobile.setText(mViewModel.getmDataManger().getUserNumber());

            binding.btnBack.setOnClickListener(this);
            new ClickShrinkEffect(binding.btnBack);
            binding.btnSave.setOnClickListener(this);
            new ClickShrinkEffect(binding.btnSave);
            binding.btnChangePhoto.setOnClickListener(this);
            new ClickShrinkEffect(binding.btnChangePhoto);
        }
    }

    @Override
    public int getBindingVariable() {
        return BR.updateProfileView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_profile;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            String fname = binding.etFirstName.getText().toString();
            String lname = binding.etLastName.getText().toString();
            String email = binding.etEmail.getText().toString();
            String number = binding.etMobile.getText().toString();

            if (fname.trim().length() == 0) {
                Toast.makeText(mContext, "Please enter first name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lname.trim().length() == 0) {
                Toast.makeText(mContext, "Please enter last name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.trim().length() == 0) {
                Toast.makeText(mContext, "Please enter your email.", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mobile", number);
                jsonObject.put("name", fname + " " + lname);
                jsonObject.put("email", email);

                updateProfile(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        }
    }

    private void updateProfile(JSONObject couponObject) {
        mViewModel.updateProfile((Activity) mContext, couponObject);
    }

    @Override
    public void onSuccessUpdateProfile(JsonObject jsonObject) {
        if (jsonObject != null) {

            if (jsonObject.get("status").getAsBoolean()) {
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                mViewModel.getmDataManger().settUserEmail(binding.etEmail.getText().toString());
                mViewModel.getmDataManger().setUsername(binding.etFirstName.getText().toString() + " " + binding.etLastName.getText().toString());
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        }
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
}