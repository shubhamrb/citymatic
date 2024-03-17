package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.orders.OrdersDataModel;
import com.mamits.citymatic.databinding.FragmentOrderDetailsBinding;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.OrderDetailsNavigator;
import com.mamits.citymatic.ui.utils.DateConvertor;
import com.mamits.citymatic.viewmodel.fragment.OrderDetailsViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class OrderDetailsFragment extends BaseFragment<FragmentOrderDetailsBinding, OrderDetailsViewModel>
        implements OrderDetailsNavigator, View.OnClickListener {

    private final String TAG = "OrderDetailsFragment";
    private FragmentOrderDetailsBinding binding;
    private Gson mGson;
    @Inject
    OrderDetailsViewModel mViewModel;
    private Context mContext;
    private OrdersDataModel orderDetailModel;

    @Override
    public OrderDetailsViewModel getMyViewModel() {
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

            Bundle bundle = getArguments();
            if (bundle != null) {
                int orderid = bundle.getInt("orderid");
//                convertDateFormat(date, time);
                fetchOrderDetails(orderid);

            }
            new ClickShrinkEffect(binding.btnBack);
            binding.btnBack.setOnClickListener(this);
        }
    }

    private void convertDateFormat(String inputDate, String time) {
        String dateString = inputDate + " " + time;
        SimpleDateFormat inputFormatter = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        try {
            Date date = inputFormatter.parse(dateString);
            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            selectedDateTime = outputFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void fetchOrderDetails(int orderid) {
        binding.shimmerLayout.startShimmer();
        mViewModel.fetchOrderSummary((Activity) mContext, orderid);
    }

    @Override
    public void onSuccessOrderDetail(JsonObject jsonObject) {
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.rootLayout.setVisibility(View.VISIBLE);

        if (jsonObject.get("status").getAsBoolean()) {


            orderDetailModel = mGson.fromJson(jsonObject.get("data").getAsJsonObject().toString(), OrdersDataModel.class);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = formatter.parse(orderDetailModel.getBooking_date_time());
                String date = new DateConvertor().getDate(d1.getTime(), DateConvertor.FORMAT_MMM_dd_yyyy_hh_mm_a);
                binding.txtDate.setText(String.format("%s", date));

            } catch (Exception e) {
                binding.txtDate.setText(orderDetailModel.getCreated_at());
                e.printStackTrace();
            }

            binding.txtProductName.setText(orderDetailModel.getOrder_detail().get(0).getName());

            binding.txtOrderId.setText("#" + orderDetailModel.getOrder_id());
            RequestOptions myOptions = new RequestOptions().override(100, 100);
            Glide.with(mContext).asBitmap().apply(myOptions).load(orderDetailModel.getOrder_detail().get(0).getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imgSubCat);


            binding.txtPrice.setText(mContext.getString(R.string.rupee) + orderDetailModel.getPayable_amount());

            String[] happyCode = String.valueOf(orderDetailModel.getHappy_code()).split("");
            binding.txtCode1.setText(happyCode[0]);
            binding.txtCode2.setText(happyCode[1]);
            binding.txtCode3.setText(happyCode[2]);
            binding.txtCode4.setText(happyCode[3]);


            binding.setCartDetailModel(orderDetailModel.getOrder_detail().get(0));
            double totalPrice = Double.parseDouble(orderDetailModel.getOrder_detail().get(0).getPrice()) * orderDetailModel.getOrder_detail().get(0).getQuantity();
            binding.setTotalPrice(totalPrice);
            binding.txtCouponPrice.setText(getString(R.string.rupee) + "00");
            binding.txtNetPrice.setText(getString(R.string.rupee) + totalPrice);
        }
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_details;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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