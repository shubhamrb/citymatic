package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.data.model.orders.OrdersDataModel;
import com.mamits.citymatic.databinding.ReviewOrdersItemListBinding;
import com.mamits.citymatic.ui.utils.DateConvertor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.OrdersViewHolder> {

    private Context mContext;
    public List<OrdersDataModel> list;
    private int SELECTED_POS = -1;
    private OrderClickListener listener;

    public ReviewOrderAdapter(Context mContex, OrderClickListener listener) {
        this.mContext = mContex;
        list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(ReviewOrdersItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        if (list.size() > 0) {
            OrdersDataModel model = list.get(position);
            holder.binding.setSelected(model.getSelected());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = formatter.parse(model.getBooking_date_time());
                String date = new DateConvertor().getDate(d1.getTime(), DateConvertor.FORMAT_MMM_dd_yyyy_hh_mm_a);
                holder.binding.txtDate.setText(String.format("%s", date));

            } catch (Exception e) {
                holder.binding.txtDate.setText(model.getCreated_at());
                e.printStackTrace();
            }

            holder.binding.txtProductName.setText(model.getOrder_detail().get(0).getName());

            RequestOptions myOptions = new RequestOptions().override(100, 100);
            Glide.with(mContext).asBitmap().apply(myOptions).load(model.getOrder_detail().get(0).getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.binding.imgSubCat);

            holder.itemView.setOnClickListener(v -> {
                if (SELECTED_POS != -1) {
                    list.get(SELECTED_POS).setSelected(false);
                    notifyItemChanged(SELECTED_POS);
                }
                list.get(position).setSelected(true);
                SELECTED_POS = holder.getBindingAdapterPosition();
                notifyItemChanged(position);

                listener.onOrderClick(model);
            });
        }
    }

    public interface OrderClickListener {
        void onOrderClick(OrdersDataModel model);
    }

    public void setList(List<OrdersDataModel> ordersList) {
        this.list.addAll(ordersList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        private ReviewOrdersItemListBinding binding;

        public OrdersViewHolder(@NonNull ReviewOrdersItemListBinding root) {
            super(root.getRoot());
            binding = root;
        }
    }
}