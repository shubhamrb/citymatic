package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.cart.CartDataModel;
import com.mamits.citymatic.databinding.FragmentCartBinding;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.adapter.CartAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.CartFragmentNavigator;
import com.mamits.citymatic.viewmodel.fragment.CartFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

public class CartFragment extends BaseFragment<FragmentCartBinding, CartFragmentViewModel> implements CartFragmentNavigator, View.OnClickListener, CartAdapter.CartClickListener {

    private final String TAG = "CartFragment";
    private FragmentCartBinding binding;
    private Gson mGson;
    @Inject
    CartFragmentViewModel mViewModel;
    private Context mContext;
    private CartAdapter cartAdapter;
    private String total_price;

    @Override
    public CartFragmentViewModel getMyViewModel() {
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

            /*start shimmer*/
            binding.shimmerLayout.startShimmer();
            fetchCartItems();

            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnCheckout);
            binding.btnBack.setOnClickListener(this);
            binding.btnCheckout.setOnClickListener(this);
        }
    }

    private void fetchCartItems() {
        mViewModel.fetchCartItems((Activity) mContext);
    }

    @Override
    public void onSuccessCartItems(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type homeData = new TypeToken<List<CartDataModel>>() {
            }.getType();
            List<CartDataModel> cartList = mGson.fromJson(jsonObject.get("data").getAsJsonObject().get("cartlist").getAsJsonArray().toString(), homeData);

            setUpCartList(cartList);

        }
    }

    @Override
    public void onSuccessCartItemRemoved(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            String message = jsonObject.get("message").getAsString();
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            fetchCartItems();
        }
    }

    @Override
    public void onSuccessCartItemUpdated(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            fetchCartItems();
        }
    }

    private void setUpCartList(List<CartDataModel> subcategories) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerViewCart.setLayoutManager(manager);
        cartAdapter = new CartAdapter(mContext, subcategories, this);
        binding.recyclerViewCart.setAdapter(cartAdapter);

        if (subcategories != null && subcategories.size() > 0) {
            float floatValue = Float.parseFloat(subcategories.get(0).getPrice()) * (subcategories.get(0).getQuantity() == 0 ? 1 : subcategories.get(0).getQuantity());
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            total_price = decimalFormat.format(floatValue);
            binding.txtTotalPrice.setText(mContext.getString(R.string.rupee) + total_price);
        }

        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerViewCart.setVisibility(View.VISIBLE);
    }


    @Override
    public int getBindingVariable() {
        return BR.cartFragmentView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cart;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        } else if (id == R.id.btn_checkout) {
            Bundle bundle = new Bundle();
            bundle.putString("total_price", total_price);
            Navigation.findNavController(((DashboardActivity) mContext).findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.nav_schedule_booking, bundle);
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
    public void plusMinusCart(int cart_id, int quantity) {
        JSONObject jsonObject = new JSONObject();
        if (quantity < 1) {
            /*remove item from cart*/
            try {
                jsonObject.put("cartid", cart_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.removeCartItem((Activity) mContext, jsonObject);
        } else {
            /*update cart*/
            try {
                jsonObject.put("cartid", cart_id);
                jsonObject.put("quantity", quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.updateCartItem((Activity) mContext, jsonObject);
        }
    }
}