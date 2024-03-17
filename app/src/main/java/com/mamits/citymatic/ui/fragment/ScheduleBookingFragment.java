package com.mamits.citymatic.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.address.AddressDataModel;
import com.mamits.citymatic.data.model.booking.SlotDataModel;
import com.mamits.citymatic.databinding.FragmentScheduleBookingBinding;
import com.mamits.citymatic.ui.activity.DashboardActivity;
import com.mamits.citymatic.ui.adapter.SlotsAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.customDialog.AddressSelectionBottomSheet;
import com.mamits.citymatic.ui.navigator.fragment.ScheduleFragmentNavigator;
import com.mamits.citymatic.viewmodel.fragment.ScheduleFragmentViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class ScheduleBookingFragment extends BaseFragment<FragmentScheduleBookingBinding, ScheduleFragmentViewModel>
        implements ScheduleFragmentNavigator, View.OnClickListener, SlotsAdapter.SlotClickListener, AddressSelectionBottomSheet.OnClickListener {

    private final String TAG = "ScheduleBookingFragment";
    private FragmentScheduleBookingBinding binding;
    private Gson mGson;
    @Inject
    ScheduleFragmentViewModel mViewModel;
    private Context mContext;
    private SlotsAdapter slotsAdapter;
    private Calendar currentDate;
    private boolean prevDateEnable = false;
    private AddressSelectionBottomSheet addressSelectionBottomSheet;
    private String selectedTime;

    @Override
    public ScheduleFragmentViewModel getMyViewModel() {
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
                String total_price = bundle.getString("total_price");
                if (total_price != null) {
                    binding.txtTotalPrice.setText(mContext.getString(R.string.rupee) + total_price);
                }
            }
            fetchBookingDates();

            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnCal);
            new ClickShrinkEffect(binding.btnPrevDate);
            new ClickShrinkEffect(binding.btnNextDate);
            new ClickShrinkEffect(binding.btnCheckout);
            binding.btnBack.setOnClickListener(this);
            binding.btnCal.setOnClickListener(this);
            binding.btnPrevDate.setOnClickListener(this);
            binding.btnNextDate.setOnClickListener(this);
            binding.btnCheckout.setOnClickListener(this);

            /*current date*/
            currentDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            String formattedDate = sdf.format(currentDate.getTime());
            binding.txtDate.setText(formattedDate);
        }
    }

    private void fetchBookingDates() {
        binding.shimmerLayout.startShimmer();
        mViewModel.fetchBookingDates((Activity) mContext);
    }

    @Override
    public void onSuccessBookingDates(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type slots = new TypeToken<List<SlotDataModel>>() {
            }.getType();
            List<SlotDataModel> slotList = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), slots);

            setUpSlotList(slotList);

        }
    }

    @Override
    public void onSuccessAddresses(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsBoolean()) {
            Type slots = new TypeToken<List<AddressDataModel>>() {
            }.getType();
            List<AddressDataModel> addressList = mGson.fromJson(jsonObject.get("data").getAsJsonObject().get("addresslist").getAsJsonArray().toString(), slots);

            if (addressSelectionBottomSheet != null && addressSelectionBottomSheet.bottomSheetMediaActionDialog.isShowing()) {
                addressSelectionBottomSheet.setAddressList(addressList);
            }
        }
    }


    private void setUpSlotList(List<SlotDataModel> slotList) {
        slotsAdapter = new SlotsAdapter(mContext, slotList, this);
        binding.recyclerViewSlots.setAdapter(slotsAdapter);

        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerViewSlots.setVisibility(View.VISIBLE);
    }


    @Override
    public int getBindingVariable() {
        return BR.scheduleFragmentView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_schedule_booking;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        } else if (id == R.id.btn_cal) {
            showDatePicker();
        } else if (id == R.id.btn_prev_date) {
            if (prevDateEnable) setPrevDate();
        } else if (id == R.id.btn_next_date) {
            setNextDate();
        } else if (id == R.id.btn_checkout) {
            selectAddress();
        }
    }

    private void selectAddress() {
        addressSelectionBottomSheet = new AddressSelectionBottomSheet(mContext, this);
        addressSelectionBottomSheet.openOption();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (addressSelectionBottomSheet != null && addressSelectionBottomSheet.bottomSheetMediaActionDialog.isShowing()) {
            getAddress(addressSelectionBottomSheet.getCurrentType());
        }
    }

    private void setNextDate() {
        prevDateEnable = true;
        binding.imgPrev.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.black, null)));
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(currentDate.getTime());
        binding.txtDate.setText(formattedDate);
    }

    private void setPrevDate() {
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(currentDate.getTime());
        binding.txtDate.setText(formattedDate);

        if (isCurrentDate()) {
            prevDateEnable = false;
            binding.imgPrev.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.gray, null)));
        }
    }

    private boolean isCurrentDate() {
        final Calendar currentCalendar = Calendar.getInstance();

        boolean isCurrentDate = (currentDate.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR))
                && (currentDate.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH))
                && (currentDate.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH));
        return isCurrentDate;
    }

    private void showDatePicker() {
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                mContext,
                (view, year1, month1, dayOfMonth) -> {
                    currentDate = Calendar.getInstance();
                    currentDate.set(year1, month1, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(currentDate.getTime());
                    binding.txtDate.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
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
    public void onSlotSelect(String time) {
        selectedTime = time;
    }

    @Override
    public void getAddress(String type) {
        mViewModel.fetchAddress((Activity) mContext, type);
    }

    @Override
    public void onContinue(AddressDataModel model) {
        /*address selected*/
        Bundle bundle = new Bundle();
        bundle.putString("Address", model.toString());
        bundle.putString("date", binding.txtDate.getText().toString());
        if (selectedTime == null && slotsAdapter.list.size() > 0) {
            selectedTime = slotsAdapter.list.get(0).getFrom_time();
        }
        bundle.putString("time", selectedTime);
        Navigation.findNavController(((DashboardActivity) mContext).findViewById(R.id.nav_host_fragment))
                .navigate(R.id.nav_order_summary, bundle);

    }

}