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
import com.mamits.citymatic.data.model.orders.OrdersDataModel;
import com.mamits.citymatic.databinding.FragmentHistoryBinding;
import com.mamits.citymatic.ui.adapter.OrderHistoryAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.HistoryFragmentNavigator;
import com.mamits.citymatic.viewmodel.fragment.HistoryFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class HistoryFragment extends BaseFragment<FragmentHistoryBinding, HistoryFragmentViewModel>
        implements HistoryFragmentNavigator, View.OnClickListener {

    private String TAG = "HistoryFragment";
    private FragmentHistoryBinding binding;

    @Inject
    HistoryFragmentViewModel mViewModel;
    private Context mContext;
    private Gson mGson;
    private List<OrdersDataModel> ordersList;
    private OrderHistoryAdapter orderHistoryAdapter;
    private final int LIMIT = 10;
    private int START_PAGE = 0;
    private int CURRENT_PAGE = START_PAGE;
    private int type = 1;

    @Override
    public HistoryFragmentViewModel getMyViewModel() {
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
                loadOrderHistory(type, CURRENT_PAGE);
            });

            binding.btnPending.setOnClickListener(v -> {
                type = 1;
                selectTab(type);
            });
            binding.btnAccepted.setOnClickListener(v -> {
                type = 2;
                selectTab(type);
            });
            binding.btnRejected.setOnClickListener(v -> {
                type = 3;
                selectTab(type);
            });
            binding.btnCancelled.setOnClickListener(v -> {
                type = 4;
                selectTab(type);
            });
            binding.btnCompleted.setOnClickListener(v -> {
                type = 5;
                selectTab(type);
            });

            setUpOrderHistoryList();
        }
        new ClickShrinkEffect(binding.btnBack);
        binding.btnBack.setOnClickListener(this);
    }

    private void selectTab(int type) {
        binding.recyclerViewOrderHistory.setVisibility(View.GONE);
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.shimmerLayout.startShimmer();
        binding.setSelectedType(type);

        if (orderHistoryAdapter != null) {
            orderHistoryAdapter.clearList();
        }
        CURRENT_PAGE = START_PAGE;
        loadOrderHistory(type, CURRENT_PAGE);
    }


    private void setUpOrderHistoryList() {
        binding.recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity());
        binding.recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);
//        mViewModel.getmNavigator().get().showProgressBars();
        selectTab(type);
    }

    private void loadOrderHistory(int type, int current_page) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start", current_page);
            jsonObject.put("pagelength", LIMIT);
            jsonObject.put("status", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.fetchOrderHistory((Activity) mContext, jsonObject);
    }


    @Override
    public int getBindingVariable() {
        return BR.historyFragmentView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
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
    public void onSuccessOrderHistory(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.get("status").getAsBoolean()) {
                mGson = new Gson();
                Type orderDataList = new TypeToken<List<OrdersDataModel>>() {
                }.getType();
                ordersList = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), orderDataList);
                if (jsonObject.get("next").getAsBoolean()) {
                    binding.btnNext.setVisibility(View.VISIBLE);
                } else {
                    binding.btnNext.setVisibility(View.GONE);
                }
                orderHistoryAdapter.setList(ordersList);
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        }
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerViewOrderHistory.setVisibility(View.VISIBLE);
    }
}