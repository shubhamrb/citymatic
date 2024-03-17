package com.mamits.citymatic.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.signup.SignUpDataModel;
import com.mamits.citymatic.databinding.ActivityRegisterBinding;
import com.mamits.citymatic.ui.base.BaseActivity;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.navigator.activity.RegisterActivityNavigator;
import com.mamits.citymatic.ui.utils.constants.AppConstant;
import com.mamits.citymatic.viewmodel.activity.RegisterActivityViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterActivityViewModel>
        implements RegisterActivityNavigator, View.OnClickListener {

    String TAG = "RegisterActivity";
    @Inject
    RegisterActivityViewModel mViewModel;
    ActivityRegisterBinding binding;
    private CustomTextView txt_resend;
    private Gson mGson;
    private String fName, lName, email, refCode;
    private boolean isCheckedTnc;
    private String mobile = "";

    @Override
    public int getBindingVariable() {
        return BR.registerView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);


        if (getIntent().hasExtra("mobile")) {
            mobile = getIntent().getStringExtra("mobile");
            binding.txtNumber.setText("+91" + mobile);
        }

        binding.btnSignup.setOnClickListener(this);

        new ClickShrinkEffect(binding.btnSignup);

        binding.chkboxTnc.setOnCheckedChangeListener((compoundButton, b) -> {
            isCheckedTnc = b;
        });
    }

    @Override
    protected RegisterActivityViewModel getMyViewModel() {
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
    public void onSuccessUserSignUp(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.get("status").getAsBoolean()) {
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                mGson = new Gson();
                SignUpDataModel model = mGson.fromJson(jsonObject.get("data").getAsJsonObject().toString(), SignUpDataModel.class);

               /* Intent intent = new Intent(this, OtpActivity.class);
                intent.putExtra("mobile", number);
                intent.putExtra("name", name);
                intent.putExtra("pass", pass);
                intent.putExtra("data", model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_signup) {
            fName = binding.etFirstName.getText().toString();
            lName = binding.etLastName.getText().toString();
            email = binding.etEmail.getText().toString();
            refCode = binding.etRefCode.getText().toString();

            if (fName.trim().length() == 0) {
                Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lName.trim().length() == 0) {
                Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.trim().length() == 0) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isCheckedTnc) {
                doSignUp(fName, lName, email, refCode);
            } else {
                Toast.makeText(this, "Please agree on terms & conditions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doSignUp(String first_name, String last_name, String email, String refCode) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (token != null && token.length() != 0) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mobile", mobile);
                    jsonObject.put("first_name", first_name);
                    jsonObject.put("last_name", last_name);
                    jsonObject.put("email", email);
                    jsonObject.put("api_key", AppConstant.API_KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mViewModel.userSignUp(this, jsonObject.toString());
            }
        });
    }
}