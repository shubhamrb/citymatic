package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.PackageItem;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;

import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ServicesViewHolder> {

    private Context mContext;
    public List<PackageItem> list;
    private boolean isSelectView;
    private int SELECTED_PLAN = 0;
    private PlanSelectListener listener;

    public PlansAdapter(Context mContext, List<PackageItem> packages, PlanSelectListener listener) {
        this.mContext = mContext;
        list = packages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.plans_item_list, parent, false);
        return new ServicesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        if (list.size() > 0) {
            PackageItem model = list.get(position);
            holder.txt_plan.setText(model.getTitle());
            holder.txt_plan_desc.setText(model.getDescription());
            holder.btn_radio.setChecked(SELECTED_PLAN == position);
            holder.txt_price.setText(mContext.getResources().getString(R.string.rupee) + model.getPrice());
            if (isSelectView) {
                holder.btn_radio.setVisibility(View.VISIBLE);
                holder.ll_price.setVisibility(View.VISIBLE);
            } else {
                holder.btn_radio.setVisibility(View.GONE);
                holder.ll_price.setVisibility(View.GONE);
            }
            holder.btn_radio.setOnClickListener(v -> {
                SELECTED_PLAN = holder.getAdapterPosition();
                listener.onPlanSelect(SELECTED_PLAN);
                notifyDataSetChanged();
            });
        }
    }

    public interface PlanSelectListener {
        void onPlanSelect(int position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeViewToSelect(boolean isSelectView) {
        SELECTED_PLAN = 0;
        this.isSelectView = isSelectView;
        notifyDataSetChanged();
    }


    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_plan, txt_price;
        private CustomTextViewHtml txt_plan_desc;
        private LinearLayout ll_price;
        private RadioButton btn_radio;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_plan = itemView.findViewById(R.id.txt_plan);
            txt_plan_desc = itemView.findViewById(R.id.txt_plan_desc);
            txt_price = itemView.findViewById(R.id.txt_price);
            ll_price = itemView.findViewById(R.id.ll_price);
            btn_radio = itemView.findViewById(R.id.btn_radio);

        }
    }
}
