package com.mamits.citymatic.ui.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.offer.OfferDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.ArrayList;
import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.OffersViewHolder> {

    private Context mContext;
    public List<OfferDataModel> list;
    private Activity activity;
    private couponSelectListener listener;


    public CouponsAdapter(Context mContex, couponSelectListener listener) {
        this.mContext = mContex;
        activity = ((Activity) mContex);
        list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.coupons_item_list, parent, false);
        return new OffersViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        if (list.size() > 0) {
            OfferDataModel model = list.get(position);

            holder.txt_offer_name.setText(model.getDescription());
            holder.txt_coupon.setText(model.getCoupon());

            holder.txt_off_rate.setText(String.format("%s%% off", model.getDiscount_amount()));

            new ClickShrinkEffect(holder.btn_apply);
            new ClickShrinkEffect(holder.btn_copy_coupon);
            holder.btn_apply.setOnClickListener(v -> {
                listener.onSelect(list.get(position));
            });
            holder.btn_copy_coupon.setOnClickListener(v -> {
                /*copy to clipboard*/
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Coupon", model.getCoupon());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, "Coupon copied.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    public interface couponSelectListener {
        void onSelect(OfferDataModel data);
    }

    public void setList(List<OfferDataModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class OffersViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_off_rate, txt_coupon;
        private CustomTextViewHtml txt_offer_name;
        private MaterialCardView btn_apply, btn_copy_coupon;

        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_offer_name = itemView.findViewById(R.id.txt_offer_name);
            txt_off_rate = itemView.findViewById(R.id.txt_off_rate);
            txt_coupon = itemView.findViewById(R.id.txt_coupon);
            btn_apply = itemView.findViewById(R.id.btn_apply);
            btn_copy_coupon = itemView.findViewById(R.id.btn_copy_coupon);
        }
    }
}
