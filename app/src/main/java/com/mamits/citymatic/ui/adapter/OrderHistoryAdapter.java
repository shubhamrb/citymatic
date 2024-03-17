package com.mamits.citymatic.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.orders.OrdersDataModel;
import com.mamits.citymatic.databinding.OrdersItemListBinding;
import com.mamits.citymatic.ui.utils.DateConvertor;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrdersViewHolder> {

    private Context mContext;
    public List<OrdersDataModel> list;
    private Activity activity;

    public OrderHistoryAdapter(Context mContex) {
        this.mContext = mContex;
        activity = ((Activity) mContex);
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrdersItemListBinding binding = OrdersItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        if (list.size() > 0) {
            OrdersDataModel model = list.get(position);
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

            holder.binding.txtOrderId.setText("#" + model.getOrder_id());
            RequestOptions myOptions = new RequestOptions().override(100, 100);
            Glide.with(mContext).asBitmap().apply(myOptions).load(model.getOrder_detail().get(0).getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.binding.imgSubCat);


            holder.binding.txtPrice.setText(mContext.getString(R.string.rupee) + model.getPayable_amount());

            String[] happyCode = String.valueOf(model.getHappy_code()).split("");
            holder.binding.txtCode1.setText(happyCode[0]);
            holder.binding.txtCode2.setText(happyCode[1]);
            holder.binding.txtCode3.setText(happyCode[2]);
            holder.binding.txtCode4.setText(happyCode[3]);


            new ClickShrinkEffect(holder.itemView);
            new ClickShrinkEffect(holder.binding.btnCall);

            holder.binding.btnCall.setOnClickListener(view -> {
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(position).getStoredetail().getMobile_number()));
                activity.startActivity(intent);*/
            });
            holder.itemView.setOnClickListener(view -> {
                gotoOrderDetail(view, position);
            });
        }
    }

    public void setList(List<OrdersDataModel> ordersList) {
        this.list.addAll(ordersList);
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }


    private void gotoOrderDetail(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderid", list.get(position).getId());

        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.nav_order_details, bundle);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        private OrdersItemListBinding binding;

        public OrdersViewHolder(@NonNull OrdersItemListBinding root) {
            super(root.getRoot());
            binding = root;
        }
    }
}