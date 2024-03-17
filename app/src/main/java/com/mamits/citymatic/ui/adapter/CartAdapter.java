package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.cart.CartDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.SubCategoryViewHolder> {

    private Context mContext;
    public List<CartDataModel> list;
    private CartClickListener listener;

    public CartAdapter(Context mContext, List<CartDataModel> cartItems, CartClickListener listener) {
        this.mContext = mContext;
        list = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.cart_item_list, parent, false);
        return new SubCategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            CartDataModel model = list.get(position);
            if (model.getService_type() != 0) {
                holder.bundle_card.setVisibility(View.VISIBLE);
            } else {
                holder.bundle_card.setVisibility(View.GONE);
            }
            holder.txt_product_name.setText(model.getProduct_name());
            holder.txt_plan.setText(model.getPackage_name());
            float floatValue = Float.parseFloat(model.getPrice()) * (model.getQuantity() == 0 ? 1 : model.getQuantity());
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String formattedFloat = decimalFormat.format(floatValue);
            holder.txt_total_price.setText(mContext.getString(R.string.rupee) + formattedFloat);
            holder.txt_price.setText(mContext.getString(R.string.rupee) + (model.getPrice()));
            holder.txt_cart_count.setText("" + model.getQuantity());
            RequestOptions myOptions = new RequestOptions().override(100, 100);
            Glide.with(mContext).asBitmap().apply(myOptions).load(model.getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_sub_cat);

            new ClickShrinkEffect(holder.btn_plus);
            new ClickShrinkEffect(holder.btn_minus);
            holder.btn_plus.setOnClickListener(v -> {
                listener.plusMinusCart(model.getId(), model.getQuantity() + 1);
            });
            holder.btn_minus.setOnClickListener(v -> {
                listener.plusMinusCart(model.getId(), model.getQuantity() - 1);
            });
        }
    }

    public interface CartClickListener {
        void plusMinusCart(int cart_id, int quantity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_product_name, txt_plan, txt_total_price, txt_price, txt_cart_count;
        private ImageView img_sub_cat;
        private CardView bundle_card, btn_plus, btn_minus;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_plan = itemView.findViewById(R.id.txt_plan);
            txt_total_price = itemView.findViewById(R.id.txt_total_price);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_cart_count = itemView.findViewById(R.id.txt_cart_count);
            bundle_card = itemView.findViewById(R.id.bundle_card);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            btn_minus = itemView.findViewById(R.id.btn_minus);

        }
    }
}
