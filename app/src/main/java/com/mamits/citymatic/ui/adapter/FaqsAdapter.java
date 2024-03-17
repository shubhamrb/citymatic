package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.product.FaqProductModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;

import java.util.List;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ServicesViewHolder> {

    private Context mContext;
    public List<FaqProductModel> list;


    public FaqsAdapter(Context mContext, List<FaqProductModel> packages) {
        this.mContext = mContext;
        list = packages;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.faq_item_list, parent, false);
        return new ServicesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        if (list.size() > 0) {
            FaqProductModel model = list.get(position);
            holder.txt_ques.setText(model.getName());
            holder.txt_ans.setText(model.getDescription());

            holder.itemView.setOnClickListener(v -> {
                if (holder.visibleAns) {
                    holder.txt_ans.setVisibility(View.GONE);
                    holder.img_arrow.setRotation(90f);
                } else {
                    holder.txt_ans.setVisibility(View.VISIBLE);
                    holder.img_arrow.setRotation(270f);
                }
                holder.visibleAns = !holder.visibleAns;
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_ques;
        private CustomTextViewHtml txt_ans;
        private ImageView img_arrow;
        private boolean visibleAns = false;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ques = itemView.findViewById(R.id.txt_ques);
            txt_ans = itemView.findViewById(R.id.txt_ans);
            img_arrow = itemView.findViewById(R.id.img_arrow);
        }
    }
}
