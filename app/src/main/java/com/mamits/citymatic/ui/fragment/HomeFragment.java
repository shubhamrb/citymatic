package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.BannerListItem;
import com.mamits.citymatic.data.model.home.CategoryListItem;
import com.mamits.citymatic.data.model.home.HomeDataModel;
import com.mamits.citymatic.data.model.home.TransportModel;
import com.mamits.citymatic.data.model.offer.OfferDataModel;
import com.mamits.citymatic.data.model.product.ProductDataModel;
import com.mamits.citymatic.databinding.FragmentHomeBinding;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.adapter.BannerPagerAdapter;
import com.mamits.citymatic.ui.adapter.BundleServiceAdapter;
import com.mamits.citymatic.ui.adapter.CategoryAdapter;
import com.mamits.citymatic.ui.adapter.LatestServicesAdapter;
import com.mamits.citymatic.ui.adapter.OfferAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.HomeFragmentNavigator;
import com.mamits.citymatic.ui.utils.constants.AppConstant;
import com.mamits.citymatic.viewmodel.fragment.HomeFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeFragmentViewModel> implements HomeFragmentNavigator, View.OnClickListener {

    private final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private Gson mGson;
    @Inject
    HomeFragmentViewModel mViewModel;
    private Context mContext;
    private CategoryAdapter categoryAdapter;
    private BundleServiceAdapter bundleServiceAdapter;
    private OfferAdapter offerAdapter;
    private List<CategoryListItem> catList;
    private List<ProductDataModel> latestServicesList;

    @Override
    public HomeFragmentViewModel getMyViewModel() {
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
        if (isRefresh) {
            mGson = new Gson();
            String action = mViewModel.getmDataManger().getNotificationType();
            mViewModel.getmDataManger().setNotificationType(null);

            if (action != null && action.trim().length() != 0) {
                final Handler handler = new Handler();
                if (action.equals("order")) {
                    handler.postDelayed(() -> {
                        Navigation.findNavController(((DashboardActivity) mContext)
                                .findViewById(R.id.nav_host_fragment)).navigate(R.id.nav_history);
                    }, 1000);
                }
            }
            /*start shimmer*/
            binding.shimmerLayout.startShimmer();

            DashboardActivity activity = ((DashboardActivity) mContext);
            if (activity != null) {
                if (activity.isListenerNull()) {
                    activity.updateLocation(this::setUpSlider);
                } else {
                    setUpSlider(mViewModel.getmDataManger().getLatitude(), mViewModel.getmDataManger().getLongitude());
                }

            }
        }
    }

    private void setUpSlider(double latitude, double longitude) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("api_key", AppConstant.API_KEY);
            jsonObject.put("userid", mViewModel.getmDataManger().getCurrentUserId());
            jsonObject.put("latitude", latitude != 0 ? latitude : "24.5695588");
            jsonObject.put("longitude", longitude != 0 ? longitude : "80.8645887");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadBanner((Activity) mContext, jsonObject);
    }


    @Override
    public int getBindingVariable() {
        return BR.homeFragmentView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onClick(View v) {

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
    public void onSuccessBanner(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type homeData = new TypeToken<HomeDataModel>() {
            }.getType();
            HomeDataModel homeDataModel = mGson.fromJson(jsonObject.get("data").getAsJsonObject().toString(), homeData);

            setUpTopBanner(homeDataModel.getBannerlist());
            setUpBottomBanner(homeDataModel.getBottom_bannerlist());
            setUpCategoryList(homeDataModel.getCategorylist());
            setUpServicesList(homeDataModel.getProductlist());
            setUpBundleServices(homeDataModel.getBundleproductlist());
            setUpOffers(homeDataModel.getCouponList());

            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.setVisibility(View.GONE);
            binding.rootLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUpTopBanner(List<BannerListItem> bannerlist) {
        /*top banner*/
        BannerPagerAdapter adapter = new BannerPagerAdapter(mContext, getChildFragmentManager(), bannerlist);
        binding.viewpagerBanner.setAdapter(adapter);
        binding.viewpagerBanner.startAutoScroll(5000);
    }

    private void setUpBottomBanner(List<BannerListItem> bannerlist) {
        /*bottom banner*/
        BannerPagerAdapter adapter = new BannerPagerAdapter(mContext, getChildFragmentManager(), bannerlist);
        binding.bottomViewpagerBanner.setAdapter(adapter);
        binding.bottomViewpagerBanner.startAutoScroll(5000);
    }

    private void setUpBundleServices(List<ProductDataModel> bundleServicesList) {
        binding.recyclerViewBundleService.setLayoutManager(new LinearLayoutManager(getActivity()));
        bundleServiceAdapter = new BundleServiceAdapter(getActivity(), bundleServicesList);
        binding.recyclerViewBundleService.setAdapter(bundleServiceAdapter);
    }

    private void setUpCategoryList(List<CategoryListItem> categorylist) {
        catList = categorylist;
        binding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categoryAdapter = new CategoryAdapter(getActivity(), catList);
        binding.recyclerViewCategory.setAdapter(categoryAdapter);
    }

    private void setUpServicesList(List<ProductDataModel> servicesList) {
        latestServicesList = servicesList;
        binding.serviceLayout.recyclerViewServices.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        LatestServicesAdapter latestServicesAdapter = new LatestServicesAdapter(mContext, servicesList);
        binding.serviceLayout.recyclerViewServices.setAdapter(latestServicesAdapter);
    }

    private void setUpOffers(List<OfferDataModel> offersList) {
        binding.recyclerViewOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (offersList.size() > 2) {
            offerAdapter = new OfferAdapter(getActivity(), offersList.subList(0, 2));
        } else {
            offerAdapter = new OfferAdapter(getActivity(), offersList);
        }
        binding.recyclerViewOffers.setAdapter(offerAdapter);
    }

}