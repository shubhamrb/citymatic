package com.mamits.citymatic.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.reviews.ReviewsDataModel;
import com.mamits.citymatic.databinding.ActivityPlayerBinding;
import com.mamits.citymatic.ui.utils.DateConvertor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayerActivity extends AppCompatActivity {
    String path;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ReviewsDataModel model;
    private ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            try {
                binding.playerView.getPlayer().stop();
                binding.playerView.getPlayer().stop(true);
                if (Util.SDK_INT >= 24) {
                    releasePlayer();
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            model = getIntent().getSerializableExtra("model", ReviewsDataModel.class);
        } else {
            model = (ReviewsDataModel) getIntent().getSerializableExtra("model");

        }
        path = model.getFile();
        Log.e("Link", path);
        if (path.toLowerCase().contains("png")
                || path.toLowerCase().contains("jpg")
                || path.toLowerCase().contains("jpeg")) {
            binding.playerView.setVisibility(View.GONE);
            binding.imageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(path).into(binding.imageView);
        } else {
            binding.imageView.setVisibility(View.GONE);
            binding.playerView.setVisibility(View.VISIBLE);
            init();
        }


        binding.rlProduct.setOnClickListener(view -> {
            /*Bundle bundle = new Bundle();
            bundle.putInt("product_id", model.getOrders().get(0).getId());
            bundle.putInt("type", model.getProduct_type());
            Navigation.findNavController(view).navigate(R.id.nav_store_detail, bundle);*/
        });
    }

    private void init() {
        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerView.setKeepContentOnPlayerReset(true);

        if (path.startsWith("https")) {
            MediaItem.Builder mediaItem = new MediaItem.Builder();
            mediaItem.setUri(path);
            player.setMediaItem(mediaItem.build());
        } else {
            MediaItem mediaItem = MediaItem.fromUri(path);
            player.setMediaItem(mediaItem);
            player.seekTo(currentWindow, playbackPosition);
        }
        binding.playerView.setPlayer(player);
        player.prepare();
        player.play();

        setUserData();
    }

    private void setUserData() {
        float rating = model.getRating();
        binding.txtRating.setText("" + rating);
        binding.txtName.setText("" + model.getUser_name().getName());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date d1 = formatter.parse(model.getCreated_at());
            String[] date = new DateConvertor().getDate(d1.getTime(), DateConvertor.FORMAT_MMM_dd_yyyy_hh_mm_a).split("\\|");
            binding.txtDate.setText("Posted on " + date[0].trim());
        } catch (Exception e) {
            binding.txtDate.setText("Posted on " + model.getCreated_at());
            e.printStackTrace();
        }
        Glide.with(this).load(model.getUser_name().getProfile_image()).into(binding.imgPerson);


        binding.txtProductName.setText(model.getOrders().get(0).getName());
        RequestOptions myOptions = new RequestOptions().override(100, 100);
        Glide.with(this).asBitmap().apply(myOptions).load(model.getOrders().get(0).getImage()).
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imgSubCat);


        binding.txtPrice.setText("Starting from "+getString(R.string.rupee) + model.getOrders().get(0).getPrice());
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        try {
            binding.playerView.getPlayer().stop();
            binding.playerView.getPlayer().stop(true);
            if (Util.SDK_INT >= 24) {
                releasePlayer();
                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        try {
            if (player != null && player.isPlaying()) {
                binding.playerView.getPlayer().stop();
                binding.playerView.getPlayer().stop(true);
                if (Util.SDK_INT >= 24) {
                    releasePlayer();
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}