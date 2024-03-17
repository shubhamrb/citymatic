package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.SubcategoryListItem;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.customviews.CustomTextViewHtml;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private Context mContext;
    public List<SubcategoryListItem> list;

    public SubCategoryAdapter(Context mContext, List<SubcategoryListItem> subcategories) {
        this.mContext = mContext;
        list = subcategories;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.subcategory_item_list, parent, false);
        return new SubCategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            SubcategoryListItem model = list.get(position);
            if (model.getPackages().size() != 0) {
                holder.bundle_card.setVisibility(View.VISIBLE);
            } else {
                holder.bundle_card.setVisibility(View.GONE);
            }
            holder.txt_sub_cat_name.setText(model.getName());
            if (model.getDefaultPackage().size() != 0) {
                holder.txt_sub_cat_price.setText("Starting from ₹" + (model.getDefaultPackage().get(0).getPrice()));
            } else {
                holder.txt_sub_cat_price.setText("Starting from ₹00");
            }
            holder.txt_sub_cat_desc.setText(model.getDescription());
            RequestOptions myOptions = new RequestOptions().override(100, 100);
            Glide.with(mContext).asBitmap().apply(myOptions).load(model.getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_sub_cat);

            new ClickShrinkEffect(holder.itemView);

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", model.getId());
                bundle.putInt("type", model.getProduct_type());
                Navigation.findNavController(view).navigate(R.id.nav_store_detail, bundle);
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_sub_cat_name, txt_sub_cat_price;
        private CustomTextViewHtml txt_sub_cat_desc;
        private ImageView img_sub_cat;
        private CardView bundle_card;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_sub_cat_name = itemView.findViewById(R.id.txt_sub_cat_name);
            txt_sub_cat_price = itemView.findViewById(R.id.txt_sub_cat_price);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);
            txt_sub_cat_desc = itemView.findViewById(R.id.txt_sub_cat_desc);
            bundle_card = itemView.findViewById(R.id.bundle_card);

        }
    }
}
