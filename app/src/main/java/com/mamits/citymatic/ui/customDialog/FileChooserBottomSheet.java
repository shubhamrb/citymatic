package com.mamits.citymatic.ui.customDialog;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.BottomsheetFileChooserBinding;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;


public class FileChooserBottomSheet {
    Context mContext;
    public BottomSheetDialog bottomSheetMediaActionDialog;
    public BottomsheetFileChooserBinding binding;
    private OnClickListener listener;
    private int order_id, service_id;
    private float rating;

    public FileChooserBottomSheet(Context mContext, int order_id, int service_id, float rating, OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.order_id = order_id;
        this.service_id = service_id;
        this.rating = rating;
    }

    public void openOption() {

        bottomSheetMediaActionDialog = new BottomSheetDialog(mContext, R.style.NoBackgroundDialogTheme);
        binding = BottomsheetFileChooserBinding.inflate(LayoutInflater.from(mContext));
        bottomSheetMediaActionDialog.setContentView(binding.getRoot());
        bottomSheetMediaActionDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.setSelectedType(1);


        new ClickShrinkEffect(binding.btnRecord);
        new ClickShrinkEffect(binding.btnGallery);
        binding.btnRecord.setOnClickListener(v -> {
            bottomSheetMediaActionDialog.dismiss();
            listener.onRecordClick(order_id, service_id, rating);
        });
        binding.btnGallery.setOnClickListener(v -> {
            bottomSheetMediaActionDialog.dismiss();
            listener.onGalleryClick(order_id, service_id, rating);
        });

        bottomSheetMediaActionDialog.setOnDismissListener(dialogInterface -> {

        });
        bottomSheetMediaActionDialog.show();

    }


    public interface OnClickListener {
        void onRecordClick(int order_id, int service_id, float rating);

        void onGalleryClick(int order_id, int service_id, float rating);
    }
}
