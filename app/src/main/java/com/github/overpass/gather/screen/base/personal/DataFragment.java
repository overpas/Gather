package com.github.overpass.gather.screen.base.personal;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.auth.register.RegistrationFragment;
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus;
import com.github.overpass.gather.screen.auth.register.add.ImageSource;
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment;
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnLongClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

// TODO: The hierarchy is messed up
public abstract class DataFragment<VM extends DataViewModel, C>
        extends RegistrationFragment<VM, C> {

    @Nullable
    @BindView(R.id.tietUsername)
    protected TextInputEditText tietUsername;
    @BindView(R.id.ivPhotoPreview)
    protected ImageView ivPhotoPreview;
    @Nullable
    @BindView(R.id.lavTick)
    protected LottieAnimationView lavTick;

    @Override
    protected void onBind() {
        super.onBind();
        getViewModel().getChosenImageData().observe(getViewLifecycleOwner(), this::handleChosenImageUri);
        getViewModel().getImageSourceData().observe(getViewLifecycleOwner(), this::handleImageSource);
        getViewModel().getWritePermissionDeniedData()
                .observe(getViewLifecycleOwner(), this::onPermissionChanged);
        getViewModel().getReadPermissionDeniedData()
                .observe(getViewLifecycleOwner(), this::onPermissionChanged);
    }

    protected void onPermissionChanged(boolean denied) {
        if (denied) {
            snackbar(ivPhotoPreview, "Sorry, you can't choose photo without permissions");
        }
    }

    protected void handleImageSource(ImageSource imageSource) {
        if (imageSource == ImageSource.GALLERY) {
            getViewModel().chooseFromGallery(getActivity(), this);
        } else if (imageSource == ImageSource.CAMERA) {
            getViewModel().chooseFromCamera(getActivity(), this);
        }
    }

    protected void handleChosenImageUri(@Nullable Uri uri) {
        if (uri != null) {
            ivPhotoPreview.setScaleX(1);
            ivPhotoPreview.setScaleY(1);
            ivPhotoPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivPhotoPreview.setImageURI(uri);
            lavTick.setVisibility(View.VISIBLE);
            lavTick.playAnimation();
        } else { // reset
            ivPhotoPreview.setScaleY(2);
            ivPhotoPreview.setScaleX(2);
            ivPhotoPreview.setScaleType(ImageView.ScaleType.CENTER);
            ivPhotoPreview.setImageResource(R.drawable.ic_add_pic);
            lavTick.setVisibility(View.GONE);
        }
    }

    protected void onChooseImageClick() {
        PickImageDialogFragment.show(getFragmentManager());
    }

    @OnLongClick(R.id.ivPhotoPreview)
    protected void resetImage() {
        getViewModel().resetChosenImage();
    }

    public void onSubmitClick() {
        getViewModel().submit(getContext().getContentResolver(), textOf(tietUsername))
                .observe(getViewLifecycleOwner(), this::handleAddDataStatus);
    }

    private void handleAddDataStatus(AddDataStatus addDataStatus) {
        switch (addDataStatus.tag()) {
            case AddDataStatus.PROGRESS:
                handleProgress(addDataStatus.as(AddDataStatus.Progress.class));
                break;
            case AddDataStatus.INVALID_USERNAME:
                handleInvalidUsername(addDataStatus.as(AddDataStatus.InvalidUsername.class));
                break;
            case AddDataStatus.ERROR:
                handleError(addDataStatus.as(AddDataStatus.Error.class));
                break;
            case AddDataStatus.SUCCESS:
                handleSuccess(addDataStatus.as(AddDataStatus.Success.class));
                break;
        }
    }

    protected void handleProgress(AddDataStatus.Progress progress) {
        ProgressDialogFragment.Companion.show(getFragmentManager());
    }

    protected void handleInvalidUsername(AddDataStatus.InvalidUsername invalidUsername) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        tietUsername.setError("Invalid username");
    }

    protected void handleError(AddDataStatus.Error error) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        String message = error.getThrowable().getLocalizedMessage();
        snackbar(ivPhotoPreview, message);
    }

    protected void handleSuccess(AddDataStatus.Success success) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getViewModel().onImageChosen(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        getViewModel().onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
