package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.databinding.FragmentAddressBinding;
import com.mamits.citymatic.ui.activity.UpdateAddressActivity;
import com.mamits.citymatic.ui.adapter.AddressAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.AddressNavigator;
import com.mamits.citymatic.viewmodel.fragment.AddressViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class AddressFragment extends BaseFragment<FragmentAddressBinding, AddressViewModel>
        implements AddressNavigator, View.OnClickListener, AddressAdapter.AddressClickListener {

    private final String TAG = "AddressFragment";
    private FragmentAddressBinding binding;
    private Gson mGson;
    @Inject
    AddressViewModel mViewModel;
    private Context mContext;
    private AddressAdapter addressAdapter;
    int cat_id;

    @Override
    public AddressViewModel getMyViewModel() {
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

            setUpAddresses();
            new ClickShrinkEffect(binding.btnBack);
            binding.btnBack.setOnClickListener(this);
        }
    }

    private void setUpAddresses() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerViewAddress.setLayoutManager(manager);
        addressAdapter = new AddressAdapter(mContext, this, true);
        binding.recyclerViewAddress.setAdapter(addressAdapter);
    }

    private void fetchAddresses() {
        mViewModel.fetchAddress((Activity) mContext, "");
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address;
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
    public void onSuccessAddresses(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type slots = new TypeToken<List<AddressDataModel>>() {
            }.getType();
            List<AddressDataModel> addressList = mGson.fromJson(jsonObject.get("data").getAsJsonObject().get("addresslist").getAsJsonArray().toString(), slots);
            addressAdapter.setList(addressList);
            binding.recyclerViewAddress.smoothScrollToPosition(0);
        }
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerViewAddress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAddressSelect(AddressDataModel model) {

    }

    @Override
    public void onSetDefault(int id) {
        mViewModel.setDefaultAddress((Activity) mContext, id);
    }

    @Override
    public void onEditAddress(int id) {
        startActivity(new Intent(mContext, UpdateAddressActivity.class).putExtra("id", id));
    }

    @Override
    public void onSuccessDefaultAddress(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            fetchAddresses();
        }
        String message = jsonObject.get("message").getAsString();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.shimmerLayout.stopShimmer();
        fetchAddresses();
    }
}