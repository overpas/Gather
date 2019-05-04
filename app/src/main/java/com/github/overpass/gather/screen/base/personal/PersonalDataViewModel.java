package com.github.overpass.gather.screen.base.personal;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.commons.SingleLiveEvent;
import com.github.overpass.gather.model.commons.image.ChooseImageHelper;
import com.github.overpass.gather.screen.auth.register.RegistrationStepViewModel;
import com.github.overpass.gather.model.data.validator.UsernameValidator;
import com.github.overpass.gather.model.repo.upload.UploadImageRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.model.usecase.userdata.PersonalDataUseCase;
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus;
import com.github.overpass.gather.screen.auth.register.add.ImageSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public abstract class PersonalDataViewModel extends RegistrationStepViewModel {

    private static final String TAG = "AddPersonalDataViewMode";
    private static final int REQUEST_CODE_FROM_GALLERY = 12;
    private static final int REQUEST_CODE_FROM_CAMERA = 13;

    private final ChooseImageHelper chooseImageHelper;
    private final PersonalDataUseCase personalDataUseCase;
    private final MutableLiveData<Uri> chosenImageData;
    private final SingleLiveEvent<Boolean> writePermissionDeniedData;
    private final SingleLiveEvent<Boolean> readPermissionDeniedData;
    private ImageSourceUseCase imageSourceUseCase;

    private Uri contentUriFromCamera;

    public PersonalDataViewModel(@NonNull Application application) {
        super(application);
        personalDataUseCase = new PersonalDataUseCase(
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new UploadImageRepo(FirebaseStorage.getInstance()),
                new UsernameValidator(),
                FirebaseAuth.getInstance()
        );
        chooseImageHelper = new ChooseImageHelper();
        chosenImageData = new MutableLiveData<>();
        writePermissionDeniedData = new SingleLiveEvent<>();
        readPermissionDeniedData = new SingleLiveEvent<>();
    }

    public LiveData<Uri> getChosenImageData() {
        return chosenImageData;
    }

    public LiveData<Boolean> getWritePermissionDeniedData() {
        return writePermissionDeniedData;
    }

    public SingleLiveEvent<Boolean> getReadPermissionDeniedData() {
        return readPermissionDeniedData;
    }

    public void firstAskPermissionsThenRun(FragmentActivity activity,
                                           Fragment fragment,
                                           int requestCode,
                                           Runnable action) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            requestPermissions(fragment, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        } else {
            // Permission has already been granted
            action.run();
        }
    }

    public void chooseFromGallery(FragmentActivity activity, Fragment fragment) {
        firstAskPermissionsThenRun(activity, fragment, REQUEST_CODE_FROM_GALLERY, () -> {
            chooseFromGallery(fragment);
        });
    }

    private void chooseFromGallery(Fragment fragment) {
        chooseImageHelper.chooseFromGallery(fragment, REQUEST_CODE_FROM_GALLERY);
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
        chosenImageData.setValue(chosenImageData.getValue());
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
        File file = chooseImageHelper.chooseFromCamera(fragment, REQUEST_CODE_FROM_CAMERA);
        Uri imageUri = Uri.parse("file://" + file.getAbsolutePath());
        chosenImageData.setValue(imageUri);
        MediaScannerConnection.scanFile(fragment.getContext(), new String[]{file.getAbsolutePath()},
                null, (path, uri) -> contentUriFromCamera = uri);
    }

    public void resetChosenImage() {
        contentUriFromCamera = null;
        chosenImageData.setValue(null);
    }

    public void chooseFromCamera(FragmentActivity activity, Fragment fragment) {
        firstAskPermissionsThenRun(activity, fragment, REQUEST_CODE_FROM_CAMERA, () -> {
            chooseFromCamera(fragment);
        });
    }

    private void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults,
                                           Fragment fragment) {
        switch (requestCode) {
            case REQUEST_CODE_FROM_CAMERA: {
                handlePermissionResult(grantResults, writePermissionDeniedData, fragment, () -> {
                    chooseFromCamera(fragment);
                });
            }
            break;
            case REQUEST_CODE_FROM_GALLERY: {
                handlePermissionResult(grantResults, readPermissionDeniedData, fragment, () -> {
                    chooseFromGallery(fragment);
                });
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void handlePermissionResult(int[] grantResults,
                                        SingleLiveEvent<Boolean> permissionDeniedData,
                                        Fragment fragment,
                                        Runnable action) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            action.run();
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            permissionDeniedData.setValue(true);
        }
    }

    public LiveData<AddDataStatus> submit(ContentResolver contentResolver, String username) {
        Uri imageUri;
        if (contentUriFromCamera != null) {
            imageUri = contentUriFromCamera;
        } else {
            imageUri = chosenImageData.getValue();
        }
        return personalDataUseCase.submit(contentResolver, username, imageUri);
    }
}
