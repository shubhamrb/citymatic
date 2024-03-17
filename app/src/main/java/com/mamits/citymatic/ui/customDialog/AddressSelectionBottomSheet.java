package com.mamits.citymatic.ui.customDialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.databinding.BottomsheetAddressSelectionBinding;
import com.mamits.citymatic.ui.activity.AddAddressActivity;
import com.mamits.citymatic.ui.adapter.AddressAdapter;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.List;


public class AddressSelectionBottomSheet implements AddressAdapter.AddressClickListener {
    Context mContext;
    public BottomSheetDialog bottomSheetMediaActionDialog;
    public BottomsheetAddressSelectionBinding binding;
    private OnClickListener listener;
    private AddressAdapter adapter;

    public AddressSelectionBottomSheet(Context mContext, OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void openOption() {

        bottomSheetMediaActionDialog = new BottomSheetDialog(mContext, R.style.NoBackgroundDialogTheme);
        binding = BottomsheetAddressSelectionBinding.inflate(LayoutInflater.from(mContext));
        bottomSheetMediaActionDialog.setContentView(binding.getRoot());
        bottomSheetMediaActionDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.setSelectedType(1);

        binding.btnHome.setOnClickListener(v -> {
            binding.setSelectedType(1);
            listener.getAddress(getCurrentType());
        });
        binding.btnWork.setOnClickListener(v -> {
            binding.setSelectedType(2);
            listener.getAddress(getCurrentType());
        });
        binding.btnOther.setOnClickListener(v -> {
            binding.setSelectedType(3);
            listener.getAddress(getCurrentType());
        });

        adapter = new AddressAdapter(mContext, this, false);
        binding.recyclerAddress.setAdapter(adapter);

        listener.getAddress(getCurrentType());

        new ClickShrinkEffect(binding.btnAddNew);
        binding.btnAddNew.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, AddAddressActivity.class));
        });
        bottomSheetMediaActionDialog.setOnDismissListener(dialogInterface -> {

        });
        bottomSheetMediaActionDialog.show();

    }

    public String getCurrentType() {
        switch (binding.getSelectedType()) {
            case 2:
                return "Work";
            case 3:
                return "Other";
            default:
                return "Home";
        }
    }

    public void setAddressList(List<AddressDataModel> addressList) {
        adapter.setList(addressList);
    }

    @Override
    public void onAddressSelect(AddressDataModel model) {
        listener.onContinue(model);
        bottomSheetMediaActionDialog.dismiss();
    }

    @Override
    public void onSetDefault(int id) {

    }

    @Override
    public void onEditAddress(int id) {

    }

    public interface OnClickListener {
        void getAddress(String type);

        void onContinue(AddressDataModel model);
    }
}
