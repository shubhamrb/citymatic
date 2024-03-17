package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.home.SubcategoryListItem;
import com.mamits.citymatic.databinding.FragmentAllSubcategoryBinding;
import com.mamits.citymatic.ui.adapter.SubCategoryAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.navigator.fragment.AllSubcategoryFragmentNavigator;
import com.mamits.citymatic.ui.utils.constants.AppConstant;
import com.mamits.citymatic.viewmodel.fragment.AllSubcategoryFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class AllSubcategoryFragment extends BaseFragment<FragmentAllSubcategoryBinding, AllSubcategoryFragmentViewModel> implements AllSubcategoryFragmentNavigator, View.OnClickListener {

    private final String TAG = "AllSubcategoryFragment";
    private FragmentAllSubcategoryBinding binding;
    private Gson mGson;
    @Inject
    AllSubcategoryFragmentViewModel mViewModel;
    private Context mContext;
    private SubCategoryAdapter subCategoryAdapter;
    int cat_id;

    @Override
    public AllSubcategoryFragmentViewModel getMyViewModel() {
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
                String name = bundle.getString("name");
                String image = bundle.getString("image");
                cat_id = bundle.getInt("cat_id", -1);

                binding.txtSubCatName.setText(name);
                Glide.with(mContext).load(image).into(binding.imgSubCat);
                fetchSubCategory();
            }

            new ClickShrinkEffect(binding.btnBack);
            binding.btnBack.setOnClickListener(this);
        }
    }

    private void fetchSubCategory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("api_key", AppConstant.API_KEY);
            jsonObject.put("category_id", cat_id);
            jsonObject.put("latitude", mViewModel.getmDataManger().getLatitude());
            jsonObject.put("longitude", mViewModel.getmDataManger().getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.fetchSubcategory((Activity) mContext, jsonObject);
    }


    private void setUpSubCategoryList(List<SubcategoryListItem> subcategories) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        binding.recyclerViewSubCategory.setLayoutManager(manager);
        subCategoryAdapter = new SubCategoryAdapter(mContext, subcategories);
        binding.recyclerViewSubCategory.setAdapter(subCategoryAdapter);
    }


    @Override
    public int getBindingVariable() {
        return BR.allSubcategoryFragmentView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_subcategory;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
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
    public void onSuccessSubcategory(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type homeData = new TypeToken<List<SubcategoryListItem>>() {
            }.getType();
            List<SubcategoryListItem> storeList = mGson.fromJson(jsonObject.get("data").getAsJsonObject().get("services").getAsJsonArray().toString(), homeData);

            setUpSubCategoryList(storeList);

        }
    }
}