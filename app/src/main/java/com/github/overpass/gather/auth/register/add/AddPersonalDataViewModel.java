package com.github.overpass.gather.auth.register.add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.github.overpass.gather.BuildConfig;
import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.github.overpass.gather.UIUtil.toast;
import static com.google.gson.internal.$Gson$Types.arrayOf;

public class AddPersonalDataViewModel extends RegistrationStepViewModel {

    private static final String TAG = "AddPersonalDataViewMode";
    private static final int REQUEST_CODE_FROM_GALLERY = 12;
    private static final int REQUEST_CODE_FROM_CAMERA = 13;
    private static final int REQUEST_CODE_WRITE_PERMISSION = 14;

    private final ChooseImageUseCase chooseImageUseCase;
    private final MutableLiveData<Uri> chosenImageData;
    private final SingleLiveEvent<Boolean> permissionDeniedData;
    private ImageSourceUseCase imageSourceUseCase;
    private String imagePath;

    public AddPersonalDataViewModel() {
        chooseImageUseCase = new ChooseImageUseCase();
        chosenImageData = new MutableLiveData<>();
        permissionDeniedData = new SingleLiveEvent<>();
    }

    public LiveData<Uri> getChosenImageData() {
        return chosenImageData;
    }

    public LiveData<Boolean> getPermissionDeniedData() {
        return permissionDeniedData;
    }

    public void chooseFromGallery(Fragment fragment) {
        chooseImageUseCase.chooseFromGallery(fragment, REQUEST_CODE_FROM_GALLERY);
    }

    public void onImageChosen(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM_GALLERY:
                    //data.getData returns the content URI for the selected Image
                    imageFromGalleryChosen(data);
                    break;
                case REQUEST_CODE_FROM_CAMERA:
                    imageFromCameraChosen();
                    break;
            }

        }
    }

    private void imageFromCameraChosen() {
        if (!TextUtils.isEmpty(imagePath)) {
            chosenImageData.setValue(Uri.parse(imagePath));
        }
    }

    public void setImageSourceUseCase(ImageSourceUseCase imageSourceUseCase) {
        this.imageSourceUseCase = imageSourceUseCase;
    }

    public LiveData<ImageSource> getImageSourceData() {
        return imageSourceUseCase.getImageSourceData();
    }

    private void imageFromGalleryChosen(@Nullable Intent data) {
        if (data != null) {
            chosenImageData.setValue(data.getData());
        }
    }

    private void chooseFromCamera(Fragment fragment) {
        imagePath = chooseImageUseCase.chooseFromCamera(fragment, REQUEST_CODE_FROM_CAMERA);
    }

    public void resetChosenImage() {
        imagePath = null;
        chosenImageData.setValue(null);
    }

    public void chooseFromCamera(FragmentActivity activity, Fragment fragment) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            permissionNotGranted(fragment);
        } else {
            // Permission has already been granted
            chooseFromCamera(fragment);
        }
    }

    public void permissionNotGranted(Fragment fragment) {
        fragment.requestPermissions(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_PERMISSION
        );
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults,
                                           Fragment fragment) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseFromCamera(fragment);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionDeniedData.setValue(true);
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void submit(String username) {

    }
}
