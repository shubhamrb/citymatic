package com.mamits.citymatic.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.offer.OfferDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OffersViewHolder> {

    private Context mContext;
    public List<OfferDataModel> list;
    private Activity activity;

    public OfferAdapter(Context mContex, List<OfferDataModel> arr) {
        this.mContext = mContex;
        activity = ((Activity) mContex);
        list = arr;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.offers_item_list, parent, false);
        return new OffersViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        if (list.size() > 0) {
            OfferDataModel model = list.get(position);

            holder.txt_offer_name.setText(model.getCoupon());
            holder.txt_des.setText(model.getDescription());

            new ClickShrinkEffect(holder.itemView);
           /* holder.itemView.setOnClickListener(v -> {
                if (listener == null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("store_id", model.getStore_id());
                    Navigation.findNavController(v).navigate(R.id.nav_store_detail, bundle);
                } else {
                    listener.onSelect(list.get(position));
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class OffersViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_offer_name;
        private CustomTextViewHtml txt_des;

        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_des = itemView.findViewById(R.id.txt_des);
            txt_offer_name = itemView.findViewById(R.id.txt_offer_name);
        }
    }
}
