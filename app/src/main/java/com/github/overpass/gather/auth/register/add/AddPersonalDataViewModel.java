package com.github.overpass.gather.auth.register.add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

import com.github.overpass.gather.auth.register.RegistrationStepViewModel;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private ImageSourceUseCase imageSourceUseCase;

    public AddPersonalDataViewModel() {
        chooseImageUseCase = new ChooseImageUseCase();
        chosenImageData = new MutableLiveData<>();
    }

    public LiveData<Uri> getChosenImageData() {
        return chosenImageData;
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
            }
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

    public void resetChosenImage() {
        chosenImageData.setValue(null);
    }

    public void chooseFromCamera(FragmentActivity activity) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            permissionNotGranted(activity);
        } else {
            // Permission has already been granted
            // TODO
            toast(activity, "Camera");
        }
    }

    public void permissionNotGranted(FragmentActivity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_PERMISSION
        );
    }
}
