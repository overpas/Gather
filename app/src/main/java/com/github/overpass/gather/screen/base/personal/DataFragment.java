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
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnLongClick;
import butterknife.Optional;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

// TODO: The hierarchy is messed up
public abstract class DataFragment<VM extends DataViewModel>
        extends RegistrationFragment<VM> {

    @Nullable
    @BindView(R.id.tietUsername)
    protected TextInputEditText tietUsername;
    @BindView(R.id.ivPhotoPreview)
    protected ImageView ivPhotoPreview;
    @Nullable
    @BindView(R.id.lavTick)
    protected LottieAnimationView lavTick;

    @Override
    protected void subscribe() {
        super.subscribe();
        viewModel.getChosenImageData().observe(getViewLifecycleOwner(), this::handleChosenImageUri);
        viewModel.getImageSourceData().observe(getViewLifecycleOwner(), this::handleImageSource);
        viewModel.getWritePermissionDeniedData()
                .observe(getViewLifecycleOwner(), this::onPermissionChanged);
        viewModel.getReadPermissionDeniedData()
                .observe(getViewLifecycleOwner(), this::onPermissionChanged);
    }

    protected void onPermissionChanged(boolean denied) {
        if (denied) {
            snackbar(ivPhotoPreview, "Sorry, you can't choose photo without permissions");
        }
    }

    protected void handleImageSource(ImageSource imageSource) {
        if (imageSource == ImageSource.GALLERY) {
            viewModel.chooseFromGallery(getActivity(), this);
        } else if (imageSource == ImageSource.CAMERA) {
            viewModel.chooseFromCamera(getActivity(), this);
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
        viewModel.resetChosenImage();
    }

    public void onSubmitClick() {
        viewModel.submit(getContext().getContentResolver(), textOf(tietUsername))
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
        ProgressDialogFragment.show(getFragmentManager());
    }

    protected void handleInvalidUsername(AddDataStatus.InvalidUsername invalidUsername) {
        ProgressDialogFragment.hide(getFragmentManager());
        tietUsername.setError("Invalid username");
    }

    protected void handleError(AddDataStatus.Error error) {
        ProgressDialogFragment.hide(getFragmentManager());
        String message = error.getThrowable().getLocalizedMessage();
        snackbar(ivPhotoPreview, message);
    }

    protected void handleSuccess(AddDataStatus.Success success) {
        ProgressDialogFragment.hide(getFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        viewModel.onImageChosen(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}