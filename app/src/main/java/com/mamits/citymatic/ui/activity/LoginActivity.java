package com.mamits.citymatic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.ActivityLoginBinding;
import com.mamits.citymatic.ui.base.BaseActivity;
import com.mamits.citymatic.ui.navigator.activity.LoginActivityNavigator;
import com.mamits.citymatic.ui.utils.constants.AppConstant;
import com.mamits.citymatic.viewmodel.activity.LoginActivityViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginActivityViewModel>
        implements LoginActivityNavigator, View.OnClickListener {

    String TAG = "LoginActivity";
    @Inject
    LoginActivityViewModel mViewModel;
    ActivityLoginBinding binding;
    private Gson mGson;
    private boolean isPassVisible = false;
    private String number;

    @Override
    public int getBindingVariable() {
        return BR.loginView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);

        if (mViewModel.getmDataManger().getCurrentUserId() != -1) {
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(dashboardIntent);
            finishAffinity();
        }

        binding.btnLogin.setOnClickListener(this);
        binding.btnIssue.setOnClickListener(this);

        new ClickShrinkEffect(binding.btnLogin);
        new ClickShrinkEffect(binding.btnIssue);
    }

    @Override
    protected LoginActivityViewModel getMyViewModel() {
        return mViewModel;
    }


    @Override
    public void showLoader() {
        showLoading();
    }

    @Override
    public void hideLoader() {
        hideLoading();
    }

    @Override
    public void checkValidation(int type, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void throwable(Throwable it) {
        it.printStackTrace();
    }

    @Override
    public void checkInternetConnection(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessUserLogin(JsonObject jsonObject) {
        if (jsonObject != null) {
            Log.e("response : ", jsonObject.toString());
            if (jsonObject.get("status").getAsBoolean()) {

                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, OtpActivity.class);
                intent.putExtra("mobile", number);
                startActivity(intent);
                finishAffinity();
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("mobile", number);
                startActivity(intent);
                finishAffinity();
            }

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_signup) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (id == R.id.btn_login) {
            number = binding.etNumber.getText().toString();

            if (number.trim().length() == 0) {
                Toast.makeText(this, "Please enter your mobile number.", Toast.LENGTH_SHORT).show();
                return;
            }

            doLogin(number);
        }
    }

    private void doLogin(String number) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            if (task.isSuccessful()) {
                String token = task.getResult();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mobile", number);
                    jsonObject.put("api_key", AppConstant.API_KEY);
                    jsonObject.put("device_type", AppConstant.DEVICE_TYPE);
                    jsonObject.put("device_token", token);
                    Log.e("params : ", jsonObject.toString());
                    mViewModel.userLogin(this, jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}