package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.PackageItem;
import com.mamits.citymatic.data.model.product.FaqProductModel;
import com.mamits.citymatic.data.model.product.ProceduresProductModel;
import com.mamits.citymatic.data.model.product.ProductDataModel;
import com.mamits.citymatic.data.model.product.ProductDetailResponse;
import com.mamits.citymatic.databinding.FragmentStoreDetailBinding;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.adapter.FaqsAdapter;
import com.mamits.citymatic.ui.adapter.PlansAdapter;
import com.mamits.citymatic.ui.adapter.ProceduresAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.StoreDetailFragmentNavigator;
import com.mamits.citymatic.ui.utils.constants.AppConstant;
import com.mamits.citymatic.viewmodel.fragment.StoreDetailFragmentViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

public class StoreDetailFragment extends BaseFragment<FragmentStoreDetailBinding, StoreDetailFragmentViewModel> implements StoreDetailFragmentNavigator, View.OnClickListener, PlansAdapter.PlanSelectListener {

    private String TAG = "StoreDetailFragment";
    private FragmentStoreDetailBinding binding;

    @Inject
    StoreDetailFragmentViewModel mViewModel;
    private Context mContext;
    private Gson mGson;
    private int product_id = -1, type = -1;
    private ProductDetailResponse response;
    private PlansAdapter planAdapter;
    private int cart_count = 0;
    private int selected_plan_position;

    @Override
    public StoreDetailFragmentViewModel getMyViewModel() {
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
            Bundle bundle = getArguments();
            if (bundle != null) {
                product_id = bundle.getInt("product_id", -1);
                type = bundle.getInt("type", -1);
                callProductDetail(product_id, type);
            }
            binding.btnAdd.setOnClickListener(this);
            binding.btnAddToCart.setOnClickListener(this);
            binding.btnPlus.setOnClickListener(this);
            binding.btnMinus.setOnClickListener(this);
            binding.rlTotalCart.setOnClickListener(this);
        }
    }

    private void callProductDetail(int product_id, int type) {
        /*start shimmer*/
        binding.shimmerLayout.startShimmer();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("api_key", AppConstant.API_KEY);
            jsonObject.put("product_id", product_id);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.fetchProductDetail((Activity) mContext, jsonObject);
    }

    @Override
    public int getBindingVariable() {
        return BR.storeDetailView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_store_detail;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            if (response != null) {
                binding.btnAdd.setVisibility(View.GONE);
                binding.txtPlanLabel.setText("Select Plan");
                planAdapter.changeViewToSelect(true);
                onPlanSelect(0);
            }
        } else if (v.getId() == R.id.btn_add_to_cart) {
            cart_count++;
            binding.txtCartCount.setText("" + cart_count);
            binding.btnAddToCart.setVisibility(View.GONE);
            binding.rlTotalCart.setVisibility(View.VISIBLE);
            binding.llPlusMinus.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btn_plus) {
            updateCartCount(cart_count + 1);
        } else if (v.getId() == R.id.btn_minus) {
            updateCartCount(cart_count - 1);
        } else if (v.getId() == R.id.rl_total_cart) {
            addToCart();
        }
    }

    private void addToCart() {
        if (response == null || cart_count == 0) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("quantity", cart_count);
            jsonObject.put("product_id", product_id);
            jsonObject.put("package_id", response.getPackages().get(selected_plan_position).getId());
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.addToCart((Activity) mContext, jsonObject);
    }

    private void updateCartCount(int cart_count) {
        this.cart_count = cart_count;
        if (this.cart_count < 1) {
            /*reset bottom cart layout*/
            binding.btnAdd.setVisibility(View.VISIBLE);
            binding.btnAddToCart.setVisibility(View.VISIBLE);
            binding.rlTotalCart.setVisibility(View.GONE);
            binding.llPlusMinus.setVisibility(View.GONE);
            binding.bottomCartLayout.setVisibility(View.GONE);
            binding.txtPlanLabel.setText("Service includes");
            planAdapter.changeViewToSelect(false);
        }
        binding.txtCartCount.setText("" + this.cart_count);
        updateTotalPrice(response.getPackages().get(this.selected_plan_position).getPrice());
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
    public void onSuccessProductDetail(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            response = mGson.fromJson(jsonObject.get("data").getAsJsonObject().toString(), ProductDetailResponse.class);
            setData();
        }
    }

    @Override
    public void onSuccessAddToCart(JsonObject jsonObject) {
        String message = jsonObject.get("message").getAsString();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(((DashboardActivity) mContext).findViewById(R.id.nav_host_fragment))
                .navigate(R.id.nav_cart);
    }

    private void setData() {
        if (response != null) {
            if (response.getProduct() != null && response.getProduct().size() != 0 && response.getProduct().get(0) != null) {

                binding.shimmerLayout.stopShimmer();
                binding.shimmerLayout.setVisibility(View.GONE);
                binding.rootLayout.setVisibility(View.VISIBLE);

                ProductDataModel productDataModel = response.getProduct().get(0);

                binding.txtProductName.setText(productDataModel.getName());
                if (response.getPackages().size() != 0) {
                    binding.bundleCard.setVisibility(View.VISIBLE);
                } else {
                    binding.bundleCard.setVisibility(View.GONE);
                }
                if (response.getProductGallery().size() != 0) {
                    Glide.with(mContext).asBitmap().load(response.getProductGallery().get(0).getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.productImg);
                }
                if (response.getDefaultPackage().size() != 0) {
                    binding.txtSubCatPrice.setText("Starting from ₹" + (response.getDefaultPackage().get(0).getPrice()));
                } else {
                    binding.txtSubCatPrice.setText("Starting from ₹00");
                }
                setUpPlansList(response.getPackages());
                setUpProcedureList(response.getProcedures());
                setUpFaqList(response.getFaqs());
            }
        }
    }

    private void setUpFaqList(List<FaqProductModel> faqList) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerFaq.setLayoutManager(manager);
        FaqsAdapter faqsAdapter = new FaqsAdapter(mContext, faqList);
        binding.recyclerFaq.setAdapter(faqsAdapter);
    }

    private void setUpProcedureList(List<ProceduresProductModel> packages) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerProcedure.setLayoutManager(manager);
        ProceduresAdapter proceduresAdapter = new ProceduresAdapter(mContext, packages);
        binding.recyclerProcedure.setAdapter(proceduresAdapter);
    }

    private void setUpPlansList(List<PackageItem> packages) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerPlans.setLayoutManager(manager);
        planAdapter = new PlansAdapter(mContext, packages, this);
        binding.recyclerPlans.setAdapter(planAdapter);
    }

    @Override
    public void onPlanSelect(int selected_plan_position) {
        this.selected_plan_position = selected_plan_position;
        if (response != null) {
            binding.bottomCartLayout.setVisibility(View.VISIBLE);
            binding.txtPrice.setText(mContext.getString(R.string.rupee) + response.getPackages().get(this.selected_plan_position).getPrice());
            updateTotalPrice(response.getPackages().get(this.selected_plan_position).getPrice());
        }
    }

    private void updateTotalPrice(String price) {
        float floatValue = Float.parseFloat(price) * (cart_count == 0 ? 1 : cart_count);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedFloat = decimalFormat.format(floatValue);
        binding.txtTotalPrice.setText(mContext.getString(R.string.rupee) + formattedFloat);
    }
}