package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.product.ProductDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.List;

public class BundleServiceAdapter extends RecyclerView.Adapter<BundleServiceAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<ProductDataModel> list;

    public BundleServiceAdapter(Context context, List<ProductDataModel> arr) {
        this.mContext = context;
        list = arr;
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
        View root = LayoutInflater.from(mContext).inflate(R.layout.bundle_service_item_list, parent, false);
        return new CategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            ProductDataModel model = list.get(position);
            holder.txt_sub_cat_name.setText(model.getName());
            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);
            Glide.with(mContext).asBitmap()
                    .apply(myOptions).load(model.getImage()).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_sub_cat);

            new ClickShrinkEffect(holder.itemView);
            /*holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", model.getId());
                Navigation.findNavController(view).navigate(R.id.nav_stores, bundle);
            });*/
        }
    }

    public void setList(List<ProductDataModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_sub_cat_name;
        private ImageView img_sub_cat;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_sub_cat_name = itemView.findViewById(R.id.txt_sub_cat_name);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);
        }
    }
}
