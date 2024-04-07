package com.mamits.citymatic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.reviews.ReviewsDataModel;
import com.mamits.citymatic.ui.customviews.CustomTextView;
import com.mamits.citymatic.ui.utils.DateConvertor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CategoryViewHolder> {

    private Context mContext;
    public List<ReviewsDataModel> list;
    private ReviewClickListener listener;

    public ReviewAdapter(Context mContex, ReviewClickListener listener) {
        this.mContext = mContex;
        list = new ArrayList<>();
        this.listener = listener;
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
        View root = LayoutInflater.from(mContext).inflate(R.layout.review_item_list, parent, false);
        return new CategoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (list.size() > 0) {
            ReviewsDataModel model = list.get(position);

            float rating = model.getRating();
            holder.txt_rating.setText("" + rating);
            holder.txt_name.setText("" + model.getUser_name().getName());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = formatter.parse(model.getCreated_at());
                String[] date = new DateConvertor().getDate(d1.getTime(), DateConvertor.FORMAT_MMM_dd_yyyy_hh_mm_a).split("\\|");
                holder.txt_date.setText("Posted on " + date[0].trim());
            } catch (Exception e) {
                holder.txt_date.setText("Posted on " + model.getCreated_at());
                e.printStackTrace();
            }
            Glide.with(mContext).load(model.getUser_name().getProfile_image()).into(holder.img_person);

            RequestOptions options = new RequestOptions().override(100, 100);
            Glide.with(mContext).load(model.getFile()).apply(options).into(holder.playerView);

            holder.itemView.setOnClickListener(view -> {
                listener.onReviewSelect(model);
            });
        }
    }

    public interface ReviewClickListener {
        void onReviewSelect(ReviewsDataModel model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ReviewsDataModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView txt_name, txt_rating, txt_date;
        private ImageView img_person, playerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_rating = itemView.findViewById(R.id.txt_rating);
            txt_date = itemView.findViewById(R.id.txt_date);
            img_person = itemView.findViewById(R.id.img_person);
            playerView = itemView.findViewById(R.id.video_view);
        }
    }
}
