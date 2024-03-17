package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.CategoryViewHolder> {

    private Context mContext;
    public List<AddressDataModel> list;
    private AddressClickListener listener;
    private boolean showAddressType;

    public AddressAdapter(Context mContex, AddressClickListener listener, boolean showAddressType) {
        this.mContext = mContex;
        list = new ArrayList<>();
        this.listener = listener;
        this.showAddressType = showAddressType;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.address_item_list, parent, false);
        return new CategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            AddressDataModel model = list.get(position);

            if (showAddressType) {
                holder.type_card.setVisibility(View.VISIBLE);
                holder.btn_edit.setVisibility(View.VISIBLE);
                holder.btn_set_default.setVisibility(View.VISIBLE);
            } else {
                new ClickShrinkEffect(holder.itemView);
                holder.type_card.setVisibility(View.GONE);
                holder.btn_edit.setVisibility(View.GONE);
                holder.btn_set_default.setVisibility(View.GONE);
            }

            if (model.getIsDefault() == 1) {
                holder.mcv_root.setCardBackgroundColor(mContext.getResources().getColor(R.color.dim_green, null));
                holder.mcv_root.setStrokeColor(mContext.getResources().getColor(R.color.color_orange, null));
                holder.btn_set_default.setVisibility(View.GONE);
                holder.default_card.setVisibility(View.VISIBLE);
                holder.type_card.setCardBackgroundColor(mContext.getResources().getColor(R.color.color_orange, null));
                holder.type_text.setTextColor(mContext.getResources().getColor(R.color.white, null));
                holder.type_icon.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.white, null)));

            } else {
                holder.mcv_root.setCardBackgroundColor(mContext.getResources().getColor(R.color.dim_gray, null));
                holder.mcv_root.setStrokeColor(mContext.getResources().getColor(R.color.dim_gray, null));
                holder.btn_set_default.setVisibility(View.VISIBLE);
                holder.default_card.setVisibility(View.GONE);
                holder.type_card.setCardBackgroundColor(mContext.getResources().getColor(R.color.ColorD9D9D9, null));
                holder.type_text.setTextColor(mContext.getResources().getColor(R.color.black, null));
                holder.type_icon.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.black, null)));
            }

            holder.txt_name.setText(model.getName());
            holder.txt_number.setText(model.getMobile() + "\n" + model.getEmail());
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
            holder.txt_address.setText(builder.toString());

            new ClickShrinkEffect(holder.btn_set_default);
            new ClickShrinkEffect(holder.btn_edit);

            holder.btn_set_default.setOnClickListener(view -> {
                listener.onSetDefault(model.getId());
            });
            holder.btn_edit.setOnClickListener(view -> {
                listener.onEditAddress(model.getId());
            });
            holder.itemView.setOnClickListener(view -> {
                listener.onAddressSelect(model);
            });
        }
    }

    public interface AddressClickListener {
        void onAddressSelect(AddressDataModel model);

        void onSetDefault(int id);

        void onEditAddress(int id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AddressDataModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_name, txt_number, txt_address, type_text;
        private MaterialCardView mcv_root, type_card, default_card, btn_edit, btn_set_default;
        private ImageView type_icon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_number = itemView.findViewById(R.id.txt_number);
            txt_address = itemView.findViewById(R.id.txt_address);
            mcv_root = itemView.findViewById(R.id.mcv_root);
            type_card = itemView.findViewById(R.id.type_card);
            type_icon = itemView.findViewById(R.id.type_icon);
            type_text = itemView.findViewById(R.id.type_text);
            default_card = itemView.findViewById(R.id.default_card);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_set_default = itemView.findViewById(R.id.btn_set_default);
        }
    }
}
