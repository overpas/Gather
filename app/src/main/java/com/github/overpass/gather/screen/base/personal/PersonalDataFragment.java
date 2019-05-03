package com.github.overpass.gather.screen.base.personal;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.auth.login.LoginActivity;
import com.github.overpass.gather.screen.auth.register.RegistrationFragment;
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus;
import com.github.overpass.gather.screen.auth.register.add.AddPersonalDataViewModel;
import com.github.overpass.gather.screen.auth.register.add.ImageSource;
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

public abstract class PersonalDataFragment<VM extends AddPersonalDataViewModel>
        extends RegistrationFragment<VM> {

    @BindView(R.id.tietUsername)
    protected TextInputEditText tietUsername;
    @BindView(R.id.ivAvatarPreview)
    protected ImageView ivAvatarPreview;
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

    private void onPermissionChanged(boolean denied) {
        if (denied) {
            snackbar(ivAvatarPreview, "Sorry, you can't choose photo without permissions");
        }
    }

    private void handleImageSource(ImageSource imageSource) {
        if (imageSource == ImageSource.GALLERY) {
            viewModel.chooseFromGallery(getActivity(), this);
        } else if (imageSource == ImageSource.CAMERA) {
            viewModel.chooseFromCamera(getActivity(), this);
        }
    }

    private void handleChosenImageUri(Uri uri) {
        if (uri != null) {
            ivAvatarPreview.setScaleX(1);
            ivAvatarPreview.setScaleY(1);
            ivAvatarPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAvatarPreview.setImageURI(uri);
            lavTick.setVisibility(View.VISIBLE);
            lavTick.playAnimation();
        } else { // reset
            ivAvatarPreview.setScaleY(2);
            ivAvatarPreview.setScaleX(2);
            ivAvatarPreview.setScaleType(ImageView.ScaleType.CENTER);
            ivAvatarPreview.setImageResource(R.drawable.ic_add_pic);
            lavTick.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ivAvatarPreview)
    protected void onChooseImageClick() {
        PickImageDialogFragment.show(getFragmentManager());
    }

    @OnLongClick(R.id.ivAvatarPreview)
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
                ProgressDialogFragment.show(getFragmentManager());
                break;
            case AddDataStatus.INVALID_USERNAME:
                ProgressDialogFragment.hide(getFragmentManager());
                tietUsername.setError("Invalid username");
                break;
            case AddDataStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                String message = addDataStatus.as(AddDataStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(ivAvatarPreview, message);
                break;
            case AddDataStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
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
