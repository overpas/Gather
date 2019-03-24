package com.github.overpass.gather.auth.register.add;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.register.RegisterViewModel;
import com.github.overpass.gather.auth.register.RegistrationFragment;
import com.github.overpass.gather.dialog.PickImageDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;

public class AddPersonalDataFragment extends RegistrationFragment<AddPersonalDataViewModel> {

    public static AddPersonalDataFragment newInstance() {
        return new AddPersonalDataFragment();
    }

    @BindView(R.id.tietUsername)
    TextInputEditText tietUsername;
    @BindView(R.id.ivAvatarPreview)
    ImageView ivAvatarPreview;
    @BindView(R.id.lavTick)
    LottieAnimationView lavTick;

    @Override
    protected AddPersonalDataViewModel createViewModel() {
        ImageSourceUseCase imageSourceUseCase = ViewModelProviders.of(getActivity())
                .get(RegisterViewModel.class)
                .getImageSourceUseCase();
        AddPersonalDataViewModel viewModel = ViewModelProviders.of(this)
                .get(AddPersonalDataViewModel.class);
        viewModel.setImageSourceUseCase(imageSourceUseCase);
        return viewModel;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_add_personal_data;
    }

    @Override
    protected void subscribe() {
        super.subscribe();
        viewModel.getChosenImageData().observe(getViewLifecycleOwner(), this::handleChosenImageUri);
        viewModel.getImageSourceData().observe(getViewLifecycleOwner(), this::handleImageSource);
        viewModel.getPermissionDeniedData().observe(getViewLifecycleOwner(), this::onPermission);
    }

    private void onPermission(Boolean denied) {
        if (denied) {
            snackbar(ivAvatarPreview, "Sorry, you can't choose photo without permissions");
        }
    }

    private void handleImageSource(ImageSource imageSource) {
        if (imageSource == ImageSource.GALLERY) {
            viewModel.chooseFromGallery(this);
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
    void onChooseImageClick() {
        PickImageDialogFragment.show(getFragmentManager());
    }

    @OnLongClick(R.id.ivAvatarPreview)
    void resetImage() {
        viewModel.resetChosenImage();
    }

    @OnClick(R.id.tvSubmit)
    public void onSubmitClick() {
        viewModel.submit(textOf(tietUsername));
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
