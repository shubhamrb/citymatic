package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.data.model.cart.CartDetailModel;
import com.mamits.citymatic.data.model.offer.OfferDataModel;
import com.mamits.citymatic.databinding.FragmentOrderSummaryBinding;
import com.mamits.citymatic.ui.activity.CouponsActivity;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.customDialog.AddressSelectionBottomSheet;
import com.mamits.citymatic.ui.navigator.fragment.OrderSummaryNavigator;
import com.mamits.citymatic.viewmodel.fragment.OrderSummaryViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class OrderSummaryFragment extends BaseFragment<FragmentOrderSummaryBinding, OrderSummaryViewModel>
        implements OrderSummaryNavigator, View.OnClickListener, AddressSelectionBottomSheet.OnClickListener {

    private final String TAG = "OrderSummaryFragment";
    private FragmentOrderSummaryBinding binding;
    private Gson mGson;
    @Inject
    OrderSummaryViewModel mViewModel;
    private Context mContext;
    private AddressSelectionBottomSheet addressSelectionBottomSheet;
    private CartDetailModel cartDetailModel;
    private OfferDataModel appliedCoupon;
    private double totalPrice = 0;
    private AddressDataModel addressDataModel;
    private String selectedDateTime = null;

    @Override
    public OrderSummaryViewModel getMyViewModel() {
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
                String strModel = bundle.getString("Address");
                String date = bundle.getString("date");
                String time = bundle.getString("time");

                convertDateFormat(date, time);

                addressDataModel = mGson.fromJson(strModel, AddressDataModel.class);
                binding.setAddressModel(addressDataModel);

                binding.setAddress(getAbsoluteAddress(addressDataModel));
            }
            fetchOrderSummary();

            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnChangeAddress);
            new ClickShrinkEffect(binding.btnCheckout);
            new ClickShrinkEffect(binding.btnAddCoupon);
            new ClickShrinkEffect(binding.btnRemove);
            binding.btnBack.setOnClickListener(this);
            binding.btnChangeAddress.setOnClickListener(this);
            binding.btnCheckout.setOnClickListener(this);
            binding.btnAddCoupon.setOnClickListener(this);
            binding.btnRemove.setOnClickListener(this);

        }
    }

    private void convertDateFormat(String inputDate, String time) {
        String dateString = inputDate + " " + time;
        SimpleDateFormat inputFormatter = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        try {
            Date date = inputFormatter.parse(dateString);
            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            selectedDateTime = outputFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getAbsoluteAddress(AddressDataModel model) {
        StringBuilder builder = new StringBuilder();

        if (model.getHouse_flat() != null && !model.getHouse_flat().isEmpty()) {
            builder.append(model.getHouse_flat()).append(", ");
        }
        if (model.getAddress() != null && !model.getAddress().isEmpty()) {
            builder.append(model.getAddress()).append(", ");
        }
        if (model.getAddress_1() != null && !model.getAddress_1().isEmpty()) {
            builder.append(model.getAddress_1()).append(", ");
        }
        if (model.getLandmark() != null && !model.getLandmark().isEmpty()) {
            builder.append(model.getLandmark()).append(", ");
        }
        if (model.getPincode() != null && !model.getPincode().isEmpty()) {
            builder.append(model.getPincode());
        }
        return builder.toString();
    }

    private void fetchOrderSummary() {
        binding.shimmerLayout.startShimmer();
        mViewModel.fetchOrderSummary((Activity) mContext);
    }

    @Override
    public void onSuccessOrderSummary(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {

            Type slots = new TypeToken<List<CartDetailModel>>() {
            }.getType();
            List<CartDetailModel> cartDetail = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), slots);
            if (cartDetail.size() != 0) {
                cartDetailModel = cartDetail.get(0);
                binding.setCartDetailModel(cartDetailModel);
                totalPrice = Double.parseDouble(cartDetailModel.getPrice()) * cartDetailModel.getQuantity();
                binding.setTotalPrice(totalPrice);
                binding.txtCouponPrice.setText(getString(R.string.rupee) + "00");
                binding.txtNetPrice.setText(getString(R.string.rupee) + totalPrice);
            }

            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.setVisibility(View.GONE);
            binding.rootLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessCouponApplied(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            binding.txtCoupon.setText(appliedCoupon.getCoupon());
            binding.setCouponApplied(true);
            String mCouponAmt = jsonObject.get("data").getAsJsonObject().get("discountamount").getAsString();
            String mPayableAmt = jsonObject.get("data").getAsJsonObject().get("finalamountpay").getAsString();

            binding.txtCouponPrice.setText(getString(R.string.rupee) + mCouponAmt);
            binding.txtNetPrice.setText(getString(R.string.rupee) + mPayableAmt);
            binding.setTotalPrice(Double.valueOf(mPayableAmt));
        }
        String message = jsonObject.get("message").getAsString();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_summary;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        } else if (id == R.id.btn_change_address) {
            addressSelectionBottomSheet = new AddressSelectionBottomSheet(mContext, this);
            addressSelectionBottomSheet.openOption();
        } else if (id == R.id.btn_add_coupon) {
            Intent intent = new Intent(mContext, CouponsActivity.class);
            intent.putExtra("order", cartDetailModel);
            startActivityForResult(intent, 1001);
        } else if (id == R.id.btn_remove) {
//            mViewModel.removeCoupon((Activity) mContext, model.getId(), mCouponAmt, mPayableAmt);
            binding.setCouponApplied(false);
            appliedCoupon = null;
            binding.txtCoupon.setText("");
        } else if (id == R.id.btn_checkout) {
            submitOrder();
        }
    }

    private void submitOrder() {
        String coupon_code = appliedCoupon != null ? appliedCoupon.getCoupon() : "";
        String address_id = String.valueOf(addressDataModel.getId());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coupon_code", coupon_code);
            jsonObject.put("address_id", address_id);
            jsonObject.put("payment_method", "COD");
            jsonObject.put("booking_date_time", selectedDateTime);
            mViewModel.submitOrder((Activity) mContext, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOrderPlaced(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Navigation.findNavController(((DashboardActivity) mContext).findViewById(R.id.nav_host_fragment)).popBackStack(R.id.nav_home, false);
            Navigation.findNavController(((DashboardActivity) mContext).findViewById(R.id.nav_host_fragment)).navigate(R.id.nav_history);

        }
        String message = jsonObject.get("message").getAsString();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("data")) {
                appliedCoupon = (OfferDataModel) data.getSerializableExtra("data");
                applyCoupon();
            }
        }
    }

    private void applyCoupon() {
        if (appliedCoupon != null) {
            mViewModel.applyCoupon((Activity) mContext, appliedCoupon.getCoupon(), String.valueOf(totalPrice));
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

    @Override
    public void getAddress(String type) {
        mViewModel.fetchAddress((Activity) mContext, type);
    }

    @Override
    public void onSuccessAddresses(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type slots = new TypeToken<List<AddressDataModel>>() {
            }.getType();
            List<AddressDataModel> addressList = mGson.fromJson(jsonObject.get("data").getAsJsonObject().get("addresslist").getAsJsonArray().toString(), slots);

            if (addressSelectionBottomSheet != null && addressSelectionBottomSheet.bottomSheetMediaActionDialog.isShowing()) {
                addressSelectionBottomSheet.setAddressList(addressList);
            }
        }
    }

    @Override
    public void onContinue(AddressDataModel model) {
        addressDataModel = model;
        binding.setAddressModel(addressDataModel);
        binding.setAddress(getAbsoluteAddress(addressDataModel));
    }
}