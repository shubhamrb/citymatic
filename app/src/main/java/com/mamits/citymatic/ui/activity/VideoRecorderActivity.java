package com.mamits.citymatic.ui.activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mamits.citymatic.ui.utils.constants.AppConstant.PREF_KEY_ACCESS_TOKEN;
import static com.mamits.citymatic.ui.utils.constants.AppConstant.PREF_NAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.IOUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;
import com.mamits.citymatic.BR;
import com.mamits.citymatic.R;
import com.mamits.citymatic.data.remote.ApiHelper;
import com.mamits.citymatic.databinding.ActivityVideoRecorderBinding;
import com.mamits.citymatic.ui.base.BaseActivity;
import com.mamits.citymatic.ui.navigator.activity.VideoRecorderNavigator;
import com.mamits.citymatic.ui.utils.listeners.ResponseListener;
import com.mamits.citymatic.viewmodel.activity.VideoRecorderViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerStates;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VideoRecorderActivity extends BaseActivity<ActivityVideoRecorderBinding, VideoRecorderViewModel> implements VideoRecorderNavigator, LinearTimer.TimerListener {

    String TAG = "VideoRecorderActivity";
    @Inject
    VideoRecorderViewModel mViewModel;
    ActivityVideoRecorderBinding binding;

    public static final int RequestPermissionCode = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    boolean isRecording = false;
    File file = null;
    private LinearTimer linearTimer;
    int time_count;
    private SharedPreferences sharedPreferences;
    private String strToken = "";
    private int order_id, service_id;
    private float rating;
    private Camera camera;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;

    @Override
    public int getBindingVariable() {
        return BR.videoRecorderViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_recorder;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding = getViewDataBinding();
        mViewModel = getMyViewModel();
        mViewModel.setNavigator(this);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, "");

        Intent intent = getIntent();
        order_id = intent.getIntExtra("order_id", -1);
        service_id = intent.getIntExtra("service_id", -1);
        rating = intent.getFloatExtra("rating", -1);


        long duration = 30 * 1000;

        linearTimer = new LinearTimer.Builder()
                .linearTimerView(binding.linearTimer)
                .duration(duration)
                .timerListener(this)
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                .build();

        binding.cameraRotate.setOnClickListener(v -> rotateCamera());
        binding.cameraFlash.setOnClickListener(v -> toggleFlash(camera));
        binding.picture.setOnClickListener(v -> {
            if (checkPermission()) {
                if (!isRecording) {
                    captureVideo();

                    binding.cameraFlash.setVisibility(View.GONE);
                    binding.cameraRotate.setVisibility(View.GONE);
                    try {
                        linearTimer.startTimer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (linearTimer.getState() == LinearTimerStates.ACTIVE) {
                        if (time_count <= 44) {
                            recording.stop();
                            binding.cameraFlash.setVisibility(View.VISIBLE);
                            binding.cameraRotate.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(this, "Minimum length should be 15sec.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                requestPermission();
            }
        });

        if (!checkPermission()) {
            requestPermission();
        } else {
            startCamera(cameraFacing);
        }
    }

    @Override
    protected VideoRecorderViewModel getMyViewModel() {
        return mViewModel;
    }

    public void startCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(VideoRecorderActivity.this);

        processCameraProvider.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = processCameraProvider.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(VideoRecorderActivity.this));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(cameraFacing);
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void captureVideo() {
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        recording = videoCapture.getOutput().prepareRecording(VideoRecorderActivity.this, options).withAudioEnabled().start(ContextCompat.getMainExecutor(VideoRecorderActivity.this), videoRecordEvent -> {
            if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                isRecording = true;
                Log.w("path==", "start");
//                capture.setEnabled(true);
            } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                isRecording = false;
                if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                    Uri fileUri = ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                    String msg = "Video capture succeeded: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                    Log.w("path==", msg);
//                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    file = getFileFromURI(VideoRecorderActivity.this, fileUri);
                    if (linearTimer.getState() == LinearTimerStates.ACTIVE) {
                        linearTimer.pauseTimer();
                    }
                    Log.w("filea", file.getAbsolutePath());
                    postReview();
                } else {
                    recording.close();
                    recording = null;
                    String msg = "Error: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public File getFileFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile;
        }
        return null;
    }

    public String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleFlash(Camera camera) {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            camera.getCameraControl().enableTorch(camera.getCameraInfo().getTorchState().getValue() == 0);
        } else {
            runOnUiThread(() -> Toast.makeText(VideoRecorderActivity.this, "Flash is not available currently", Toast.LENGTH_SHORT).show());
        }
    }

    public void rotateCamera() {
        if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
            cameraFacing = CameraSelector.LENS_FACING_FRONT;
        } else {
            cameraFacing = CameraSelector.LENS_FACING_BACK;
        }
        startCamera(cameraFacing);
    }

    private void postReview() {
        mViewModel.getmNavigator().get().showLoader();
        try {
            RequestBody order_id_body;
            MultipartBody.Part fileDoc = null;

            order_id_body = RequestBody.create(MultipartBody.FORM, String.valueOf(order_id));
            RequestBody service_id_body = RequestBody.create(MultipartBody.FORM, String.valueOf(service_id));
            RequestBody rating_body = RequestBody.create(MultipartBody.FORM, String.valueOf(rating));
            if (file != null)
                fileDoc = MultipartBody.Part.createFormData("ratinfileg", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

            new ApiHelper().postReview(this, strToken, order_id_body, service_id_body, rating_body, fileDoc, new ResponseListener() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    mViewModel.getmNavigator().get().hideLoader();
                    String message = jsonObject.get("message").getAsString();
                    Toast.makeText(VideoRecorderActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailed(Throwable throwable) {
                    mViewModel.getmNavigator().get().hideLoader();
                    Toast.makeText(VideoRecorderActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } catch (Exception e) {
            mViewModel.getmNavigator().get().hideLoader();
            e.printStackTrace();
        }
    }


    @Override
    public void animationComplete() {
        linearTimer.resetTimer();
    }

    @Override
    public void timerTick(long tickUpdateInMillis) {
        time_count = (int) (tickUpdateInMillis / 1000);
        Log.w("timertimer", "" + tickUpdateInMillis + " :" + time_count);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(tickUpdateInMillis),
                TimeUnit.MILLISECONDS.toSeconds(tickUpdateInMillis)
                        - TimeUnit.MINUTES
                        .toSeconds(TimeUnit.MILLISECONDS.toHours(tickUpdateInMillis)));

        binding.time.setText(formattedTime);
    }

    @Override
    public void onTimerReset() {
        binding.time.setText("");
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
    public void onSuccessAddressAdded(JsonObject jsonObject) {

    }

}