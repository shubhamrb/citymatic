package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.notification.NotificationModel;
import com.mamits.citymatic.databinding.FragmentNotificationBinding;
import com.mamits.citymatic.ui.adapter.NotificationAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.NotificationFragmentNavigator;
import com.mamits.citymatic.viewmodel.fragment.NotificationFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel> implements NotificationFragmentNavigator, View.OnClickListener {

    private String TAG = "NotificationFragment";
    private FragmentNotificationBinding binding;

    @Inject
    NotificationFragmentViewModel mViewModel;
    private Context mContext;
    private Gson mGson;
    private final int LIMIT = 10;
    private int START_PAGE = 0;
    private int CURRENT_PAGE = START_PAGE;
    private NotificationAdapter notificationAdapter;

    @Override
    public NotificationFragmentViewModel getMyViewModel() {
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
            binding.btnNext.setOnClickListener(view1 -> {
                CURRENT_PAGE++;
                loadNotification(CURRENT_PAGE);
            });
            setUpNotificationList();
        }
        new ClickShrinkEffect(binding.btnBack);
        binding.btnBack.setOnClickListener(this);
    }

    private void setUpNotificationList() {
        binding.recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationAdapter = new NotificationAdapter(getActivity());
        binding.recyclerViewNotification.setAdapter(notificationAdapter);
        /*start shimmer*/
        binding.shimmerLayout.startShimmer();
        loadNotification(CURRENT_PAGE);
    }

    private void loadNotification(int current_page) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start", current_page);
            jsonObject.put("pagelength", LIMIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.fetchNotification((Activity) mContext, jsonObject);
    }


    @Override
    public int getBindingVariable() {
        return BR.notificationView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
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

    @Override
    public void onSuccessNotification(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.get("status").getAsBoolean()) {
                mGson = new Gson();
                Type notification = new TypeToken<List<NotificationModel>>() {
                }.getType();
                List<NotificationModel> notificationList = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), notification);

                if (jsonObject.get("next").getAsBoolean()) {
                    binding.btnNext.setVisibility(View.VISIBLE);
                } else {
                    binding.btnNext.setVisibility(View.GONE);
                }
                notificationAdapter.setList(notificationList);
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        }
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerViewNotification .setVisibility(View.VISIBLE);
    }


}