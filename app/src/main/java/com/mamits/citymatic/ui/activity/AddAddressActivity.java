package com.mamits.citymatic.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.databinding.ActivityAddAddressBinding;
import com.mamits.citymatic.ui.base.BaseActivity;
import com.mamits.citymatic.ui.navigator.activity.AddAddressActivityNavigator;
import com.mamits.citymatic.viewmodel.activity.AddAddressViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class AddAddressActivity extends BaseActivity<ActivityAddAddressBinding, AddAddressViewModel> implements AddAddressActivityNavigator, View.OnClickListener {

    String TAG = "AddAddressActivity";
    @Inject
    AddAddressViewModel mViewModel;
    ActivityAddAddressBinding binding;
    private Gson mGson;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public int getBindingVariable() {
        return BR.addAddressView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);

        binding.setLocationFetch(true);
        binding.setIsManual(false);
        binding.setSelectedType(1);

        new ClickShrinkEffect(binding.btnHome);
        new ClickShrinkEffect(binding.btnWork);
        new ClickShrinkEffect(binding.btnOther);
        new ClickShrinkEffect(binding.btnSignup);
        new ClickShrinkEffect(binding.btnManualLocation);
        new ClickShrinkEffect(binding.btnCaptureAutomatically);

        binding.btnHome.setOnClickListener(this);
        binding.btnWork.setOnClickListener(this);
        binding.btnOther.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
        binding.btnManualLocation.setOnClickListener(this);
        binding.btnCaptureAutomatically.setOnClickListener(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationPermission();
    }

    private void requestLocationPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = null;
            Boolean coarseLocationGranted = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
            }

            if (fineLocationGranted != null && fineLocationGranted) {
                // Precise location access granted.
                getLocation();
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                // Only approximate location access granted.
                getLocation();
            } else {
                // No location access granted.
            }
        });

        locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    Log.i(TAG, "lat : " + addresses.get(0).getLatitude() + " , " + "long : " + addresses.get(0).getLongitude());
                    Log.i(TAG, "address : " + addresses.get(0).getAddressLine(0) + " , " + "city : " + addresses.get(0).getLocality());

                    binding.txtFetchedLocation.setText(addresses.get(0).getAddressLine(0));

                    setFetchedDataInFields(addresses.get(0));
                    binding.setLocationFetch(false);
                    binding.setIsManual(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setFetchedDataInFields(Address address) {
        if (address != null) {
            Log.e(TAG, String.valueOf(address));
            binding.etAddress1.setText(address.getAddressLine(0));
            binding.etPinCode.setText(address.getPostalCode());
        }
    }

    @Override
    protected AddAddressViewModel getMyViewModel() {
        return mViewModel;
    }


    @Override
    public void showLoader() {
        showLoading();
    }

    @Override
    public void hideLoader() {
        hideLoading();
    }

    @Override
    public void checkValidation(int type, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void throwable(Throwable it) {
        it.printStackTrace();
    }

    @Override
    public void checkInternetConnection(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_signup) {
            String house_flat = binding.etHouseNo.getText().toString();
            String landmark = binding.etLandmark.getText().toString();
            String address1 = binding.etAddress1.getText().toString();
            String address2 = binding.etAddress2.getText().toString();
            String address_type = "Home";
            String pincode = binding.etPinCode.getText().toString();


            switch (binding.getSelectedType()) {
                case 1:
                    address_type = "Home";
                    break;
                case 2:
                    address_type = "Work";
                    break;
                case 3:
                    address_type = "Other";
                    break;
            }

            if (house_flat.trim().length() == 0) {
                Toast.makeText(this, "Please enter your House or flat no.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (landmark.trim().length() == 0) {
                Toast.makeText(this, "Please enter the landmark.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (address1.trim().length() == 0) {
                Toast.makeText(this, "Please enter your address.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pincode.trim().length() == 0) {
                Toast.makeText(this, "Please enter your pin code.", Toast.LENGTH_SHORT).show();
                return;
            }

            addAddress(house_flat, landmark, address1, address2, address_type, pincode);

        } else if (id == R.id.btn_home) {
            binding.setSelectedType(1);
        } else if (id == R.id.btn_work) {
            binding.setSelectedType(2);
        } else if (id == R.id.btn_other) {
            binding.setSelectedType(3);
        } else if (id == R.id.btn_manual_location) {
            binding.setLocationFetch(false);
            binding.setIsManual(true);
        } else if (id == R.id.btn_capture_automatically) {
            if (binding.getIsManual()) {
                binding.setLocationFetch(true);
                binding.setIsManual(false);
                getLocation();
            } else {
                binding.etAddress1.setText("");
                binding.etPinCode.setText("");
                binding.setLocationFetch(false);
                binding.setIsManual(true);
            }
        }
    }

    private void addAddress(String house_flat, String landmark, String address1,
                            String address2, String address_type, String pincode) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("house_flat", house_flat);
            jsonObject.put("landmark", landmark);
            jsonObject.put("address1", address1);
            jsonObject.put("address2", address2);
            jsonObject.put("address_type", address_type);
            jsonObject.put("pincode", pincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.addAddress(this, jsonObject.toString());
    }

    @Override
    public void onSuccessAddressAdded(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.get("status").getAsBoolean()) {
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onSuccessAddressUpdated(JsonObject jsonObject) {

    }

    @Override
    public void onSuccessGetAddress(JsonObject jsonObject) {

    }
}