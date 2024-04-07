package com.mamits.citymatic.ui.customDialog;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.BottomsheetRatingBinding;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;


public class RatingBottomSheet {
    Context mContext;
    public BottomSheetDialog bottomSheetMediaActionDialog;
    public BottomsheetRatingBinding binding;
    private OnClickListener listener;
    private int order_id, service_id;

    public RatingBottomSheet(Context mContext, int order_id, int service_id, OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.order_id = order_id;
        this.service_id = service_id;
    }

    public void openOption() {

        bottomSheetMediaActionDialog = new BottomSheetDialog(mContext, R.style.NoBackgroundDialogTheme);
        binding = BottomsheetRatingBinding.inflate(LayoutInflater.from(mContext));
        bottomSheetMediaActionDialog.setContentView(binding.getRoot());
        bottomSheetMediaActionDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.setSelectedType(1);


        new ClickShrinkEffect(binding.btnContinue);
        binding.btnContinue.setOnClickListener(v -> {
            bottomSheetMediaActionDialog.dismiss();
            listener.onContinue(order_id, service_id, binding.ratingBar.getRating());
        });

        bottomSheetMediaActionDialog.setOnDismissListener(dialogInterface -> {

        });
        bottomSheetMediaActionDialog.show();

    }

    public interface OnClickListener {
        void onContinue(int order_id, int service_id, float rating);
    }
}
