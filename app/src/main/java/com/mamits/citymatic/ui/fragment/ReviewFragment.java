package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.reviews.ReviewsDataModel;
import com.mamits.citymatic.databinding.FragmentReviewBinding;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.activity.PlayerActivity;
import com.mamits.citymatic.ui.adapter.ReviewAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.customviews.CenterDecoration;
import com.mamits.citymatic.ui.customviews.CenterZoomLayoutManager;
import com.mamits.citymatic.ui.navigator.fragment.ReviewNavigator;
import com.mamits.citymatic.viewmodel.fragment.ReviewViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class ReviewFragment extends BaseFragment<FragmentReviewBinding, ReviewViewModel>
        implements ReviewNavigator, View.OnClickListener, ReviewAdapter.ReviewClickListener {

    private final String TAG = "ReviewFragment";
    private FragmentReviewBinding binding;
    private Gson mGson;
    @Inject
    ReviewViewModel mViewModel;
    private Context mContext;
    private ReviewAdapter reviewAdapter;
    private boolean isMyReview;
    private int CURRENT_POSITION = 0;

    @Override
    public ReviewViewModel getMyViewModel() {
        return mViewModel;
    }

    @Override
    protected void initView(View view, boolean isRefresh) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);
        if (getActivity() != null) {
            mContext = getActivity();
        } else if (getBaseActivity() != null) {
            mContext = getBaseActivity();
        } else if (view.getContext() != null) {
            mContext = view.getContext();
        }
        binding.setIsMyReview(isMyReview);
        if (isRefresh) {
            mGson = new Gson();

            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnMyReview);
            new ClickShrinkEffect(binding.btnDelete);
            new ClickShrinkEffect(binding.btnPost);
            binding.btnBack.setOnClickListener(this);
            binding.btnMyReview.setOnClickListener(this);
            binding.btnDelete.setOnClickListener(this);
            binding.btnPost.setOnClickListener(this);
            setUpPublicReviews();
        }
    }

    private void setUpPublicReviews() {
        binding.recyclerReview.addItemDecoration(new CenterDecoration(0));
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerReview);
        CenterZoomLayoutManager manager = new CenterZoomLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerReview.setLayoutManager(manager);
        reviewAdapter = new ReviewAdapter(mContext, this);
        binding.recyclerReview.setAdapter(reviewAdapter);
        binding.recyclerReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                CURRENT_POSITION = newState;
            }
        });
    }

    private void fetchReviews() {
        binding.shimmerLayout.startShimmer();
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.recyclerReview.setVisibility(View.INVISIBLE);
        binding.btnPost.setVisibility(View.GONE);
        binding.llDelEdit.setVisibility(View.GONE);

        mViewModel.fetchPublicReviews((Activity) mContext);
    }

    private void fetchMyReviews() {
        binding.shimmerLayout.startShimmer();
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.recyclerReview.setVisibility(View.INVISIBLE);
        binding.btnPost.setVisibility(View.GONE);
        binding.llDelEdit.setVisibility(View.GONE);
        mViewModel.fetchMyReviews((Activity) mContext);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_review;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            if (binding.getIsMyReview()) {
                binding.txtToolbar.setText("Testimonials");
                ((DashboardActivity) mContext).hideBottomNav(false);
                isMyReview = false;
                fetchReviews();
            } else {
                Navigation.findNavController(v).popBackStack();
            }
        } else if (id == R.id.btn_my_review) {
            binding.txtToolbar.setText("My Reviews");
            ((DashboardActivity) mContext).hideBottomNav(true);
            isMyReview = true;
            fetchMyReviews();
        } else if (id == R.id.btn_delete) {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete Review!")
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        mViewModel.deleteReview((Activity) mContext,
                                reviewAdapter.list.get(CURRENT_POSITION).getId());
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
        } else if (id == R.id.btn_post) {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.nav_post_review);
        }
    }

    @Override
    public void showProgressBars() {
        showsLoading();
    }

    @Override
    public void checkInternetConnection(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBars() {
        hidesLoading();
    }

    @Override
    public void checkValidation(int errorCode, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void throwable(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onSuccessReviews(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type slots = new TypeToken<List<ReviewsDataModel>>() {
            }.getType();
            List<ReviewsDataModel> addressList = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), slots);
            reviewAdapter.setList(addressList);
            binding.recyclerReview.smoothScrollToPosition(0);

        }
        binding.setIsMyReview(isMyReview);
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerReview.setVisibility(View.VISIBLE);
        binding.btnPost.setVisibility(View.VISIBLE);

        /*reset position*/
        CURRENT_POSITION = 0;
        if (isMyReview) {
            if (reviewAdapter.list.size() == 0) {
                binding.llDelEdit.setVisibility(View.GONE);
            } else {
                binding.llDelEdit.setVisibility(View.VISIBLE);
            }
        } else {
            binding.llDelEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReviewSelect(ReviewsDataModel model) {
        try {
            startActivity(new Intent(getContext(), PlayerActivity.class)
                    .putExtra("model", model));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessReviewDeleted(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            fetchMyReviews();
        }
        String message = jsonObject.get("message").getAsString();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.shimmerLayout.stopShimmer();
        fetchReviews();
    }
}