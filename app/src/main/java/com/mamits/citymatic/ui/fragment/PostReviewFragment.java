package com.mamits.citymatic.ui.fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.model.orders.OrdersDataModel;
import com.mamits.citymatic.data.remote.ApiHelper;
import com.mamits.citymatic.databinding.FragmentPostReviewBinding;
import com.mamits.citymatic.ui.activity.VideoRecorderActivity;
import com.mamits.citymatic.ui.adapter.ReviewOrderAdapter;
import com.mamits.citymatic.ui.base.BaseFragment;
import com.mamits.citymatic.ui.customDialog.FileChooserBottomSheet;
import com.mamits.citymatic.ui.customDialog.RatingBottomSheet;
import com.mamits.citymatic.ui.navigator.fragment.PostReviewNavigator;
import com.mamits.citymatic.ui.utils.listeners.ResponseListener;
import com.mamits.citymatic.viewmodel.fragment.PostReviewViewModel;
import com.realpacific.clickshrinkeffect.ClickShrinkEffect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostReviewFragment extends BaseFragment<FragmentPostReviewBinding, PostReviewViewModel>
        implements PostReviewNavigator, View.OnClickListener, ReviewOrderAdapter.OrderClickListener,
        RatingBottomSheet.OnClickListener, FileChooserBottomSheet.OnClickListener {

    private final String TAG = "PostReviewFragment";
    private FragmentPostReviewBinding binding;
    private Gson mGson;
    @Inject
    PostReviewViewModel mViewModel;
    private Context mContext;
    private ReviewOrderAdapter reviewAdapter;
    private final int REQUEST_CODE_VIDEO = 100;
    private ActivityResultLauncher<String[]> somePermissionResultLauncher;
    private File file = null;
    private final int LIMIT = 10;
    private int START_PAGE = 0;
    private int CURRENT_PAGE = START_PAGE;
    private int type = 5;

    @Override
    public PostReviewViewModel getMyViewModel() {
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

            new ClickShrinkEffect(binding.btnBack);
            new ClickShrinkEffect(binding.btnCancel);
            new ClickShrinkEffect(binding.btnPost);
            binding.btnBack.setOnClickListener(this);
            binding.btnCancel.setOnClickListener(this);
            binding.btnPost.setOnClickListener(this);
            binding.btnNext.setOnClickListener(view1 -> {
                CURRENT_PAGE++;
                fetchOrders(type, CURRENT_PAGE);
            });
            setUpPublicReviews();
        }

        somePermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
            boolean granted = true;
            for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                if (!x.getValue()) granted = false;
            }
            if (granted) {
                startActivity(new Intent(getContext(), VideoRecorderActivity.class));
            } else {
                Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setUpPublicReviews() {
        reviewAdapter = new ReviewOrderAdapter(mContext, this);
        binding.recyclerOrders.setAdapter(reviewAdapter);
        binding.shimmerLayout.stopShimmer();
        CURRENT_PAGE = START_PAGE;
        reviewAdapter.list.clear();
        fetchOrders(type, CURRENT_PAGE);
    }

    private void fetchOrders(int type, int current_page) {
        binding.shimmerLayout.startShimmer();
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.recyclerOrders.setVisibility(View.INVISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start", current_page);
            jsonObject.put("pagelength", LIMIT);
            jsonObject.put("status", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.fetchOrders((Activity) mContext, jsonObject);
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_post_review;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            Navigation.findNavController(v).popBackStack();
        } else if (id == R.id.btn_cancel) {
            file = null;
            binding.mcvFileSelectedView.setVisibility(View.GONE);
        } else if (id == R.id.btn_post) {
            postReview();
        }
    }

    private void postReview() {
        try {
            mViewModel.getmNavigator().get().showProgressBars();
            String order_id = binding.tvOrderId.getText().toString();
            String service_id = binding.tvServiceId.getText().toString();
            String rating = binding.rvRating.getText().toString();
            RequestBody order_id_body;
            MultipartBody.Part fileDoc = null;

            order_id_body = RequestBody.create(MultipartBody.FORM, order_id);
            RequestBody service_id_body = RequestBody.create(MultipartBody.FORM, service_id);
            RequestBody rating_body = RequestBody.create(MultipartBody.FORM, rating);
            if (file != null)
                fileDoc = MultipartBody.Part.createFormData("ratinfileg", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

            new ApiHelper().postReview((Activity) mContext, mViewModel.getmDataManger().getAccessToken(), order_id_body, service_id_body, rating_body, fileDoc, new ResponseListener() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    mViewModel.getmNavigator().get().hideProgressBars();
                    String message = jsonObject.get("message").getAsString();
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    binding.btnBack.performClick();
                }

                @Override
                public void onFailed(Throwable throwable) {
                    mViewModel.getmNavigator().get().hideProgressBars();
                    Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            mViewModel.getmNavigator().get().hideProgressBars();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_VIDEO) {
                Uri selectedVideoUri = data.getData();
                // Handle the selected video file
                try {
                    file = uriToFile(selectedVideoUri);
                    if (file != null) {
                        binding.tvFileName.setText(file.getName());
                        binding.mcvFileSelectedView.setVisibility(View.VISIBLE);
                        RequestOptions options = new RequestOptions().override(100, 100);
                        Glide.with(mContext).load(selectedVideoUri).apply(options).into(binding.imgSelectedVideo);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File uriToFile(Uri uri) {
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            String fileName = getFileNameFromUri(getContext(), uri);
            if (fileName != null) {
                file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                inputStream = getContext().getContentResolver().openInputStream(uri);
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            file = null; // Set file to null in case of an exception
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    private String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return fileName;
    }


    public boolean checkPermissionCamera() {
        int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionCamera() {
        somePermissionResultLauncher.launch(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, RECORD_AUDIO});
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
    public void onSuccessOrderHistory(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.get("status").getAsBoolean()) {
                mGson = new Gson();
                Type orderDataList = new TypeToken<List<OrdersDataModel>>() {
                }.getType();
                List<OrdersDataModel> ordersList = mGson.fromJson(jsonObject.get("data").getAsJsonArray().toString(), orderDataList);
                if (jsonObject.get("next").getAsBoolean()) {
                    binding.btnNext.setVisibility(View.VISIBLE);
                } else {
                    binding.btnNext.setVisibility(View.GONE);
                }
                reviewAdapter.setList(ordersList);
            } else {
                int messageId = jsonObject.get("messageId").getAsInt();
                String message = jsonObject.get("message").getAsString();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        }
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.recyclerOrders.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onOrderClick(OrdersDataModel model) {
        RatingBottomSheet ratingBottomSheet = new RatingBottomSheet(mContext, model.getId(), model.getService_id(), this);
        ratingBottomSheet.openOption();
    }


    @Override
    public void onContinue(int order_id, int service_id, float rating) {
        FileChooserBottomSheet fileChooserBottomSheet = new FileChooserBottomSheet(mContext, order_id, service_id, rating, this);
        fileChooserBottomSheet.openOption();
    }

    @Override
    public void onRecordClick(int order_id, int service_id, float rating) {
        if (checkPermissionCamera()) {
            Intent intent = new Intent(getContext(), VideoRecorderActivity.class);
            intent.putExtra("order_id", order_id);
            intent.putExtra("service_id", service_id);
            intent.putExtra("rating", rating);
            startActivity(intent);
        } else {
            requestPermissionCamera();
        }
    }

    @Override
    public void onGalleryClick(int order_id, int service_id, float rating) {
        binding.tvOrderId.setText("" + order_id);
        binding.tvServiceId.setText("" + service_id);
        binding.rvRating.setText("" + rating);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*"); // Set the MIME type to filter video files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }
}