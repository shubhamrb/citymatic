package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.CategoryListItem;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    public List<CategoryListItem> list;

    public CategoryAdapter(Context mContex, List<CategoryListItem> categoryList) {
        this.mContext = mContex;
        list = categoryList;
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
        View root = LayoutInflater.from(mContext).inflate(R.layout.category_item_list, parent, false);
        return new CategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            CategoryListItem model = list.get(position);
            holder.txt_cat_name.setText(model.getName());

//            holder.txt_des.setText(model.getDescription());
//            holder.background_card.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model.getBg_color())));
            Glide.with(mContext).load(model.getImage()).into(holder.img_cat);
            new ClickShrinkEffect(holder.itemView);

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("cat_id", model.getId());
                bundle.putString("name", model.getName());
                bundle.putString("image", model.getImage());
                Navigation.findNavController(view).navigate(R.id.nav_all_subcategory, bundle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 6);
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_cat_name, txt_des;
        private ImageView img_cat;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_cat_name = itemView.findViewById(R.id.txt_cat_name);
            img_cat = itemView.findViewById(R.id.img_cat);
            txt_des = itemView.findViewById(R.id.txt_des);

        }
    }
}
