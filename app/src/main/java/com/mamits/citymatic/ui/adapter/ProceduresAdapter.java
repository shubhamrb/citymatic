package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.product.ProceduresProductModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;

import java.util.List;

public class ProceduresAdapter extends RecyclerView.Adapter<ProceduresAdapter.ServicesViewHolder> {

    private Context mContext;
    public List<ProceduresProductModel> list;


    public ProceduresAdapter(Context mContext, List<ProceduresProductModel> packages) {
        this.mContext = mContext;
        list = packages;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.procedure_item_list, parent, false);
        return new ServicesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        if (list.size() > 0) {
            ProceduresProductModel model = list.get(position);
            holder.txt_number.setText("0" + (position + 1));
            holder.txt_title.setText(model.getName());
            holder.txt_desc.setText(model.getDescription());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_number, txt_title;
        private CustomTextViewHtml txt_desc;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_number = itemView.findViewById(R.id.txt_number);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_desc = itemView.findViewById(R.id.txt_desc);

        }
    }
}
