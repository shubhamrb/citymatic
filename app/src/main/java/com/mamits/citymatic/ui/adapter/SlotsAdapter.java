package com.mamits.citymatic.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.booking.SlotDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.utils.commonClasses.TimeUtils;

import java.util.List;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.SubCategoryViewHolder> {

    private Context mContext;
    public List<SlotDataModel> list;
    private SlotClickListener listener;
    private int old_position;
    private int selected_position;

    public SlotsAdapter(Context mContext, List<SlotDataModel> cartItems, SlotClickListener listener) {
        this.mContext = mContext;
        list = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.slot_item_list, parent, false);
        return new SubCategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            SlotDataModel model = list.get(position);

            String beautifyDate = TimeUtils.getBeautifiedSlotTime(model.getFrom_time(), "hh:mm aa");
            holder.txt_time.setText(beautifyDate.toLowerCase());

            holder.btn_radio.setChecked(selected_position == position);

            holder.itemView.setOnClickListener(v -> {
                old_position = selected_position;
                selected_position = holder.getAdapterPosition();
                holder.btn_radio.setChecked(true);
                ((Activity) mContext).runOnUiThread(() -> {
                    notifyItemChanged(old_position);
                    listener.onSlotSelect(model.getFrom_time());
                });
            });
        }
    }

    public interface SlotClickListener {
        void onSlotSelect(String time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_time;
        private RadioButton btn_radio;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_time = itemView.findViewById(R.id.txt_time);
            btn_radio = itemView.findViewById(R.id.btn_radio);
        }
    }
}
