package com.github.overpass.gather.screen.profile;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.auth.login.LoginActivity;
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus;
import com.github.overpass.gather.screen.base.BackPressFragment;
import com.github.overpass.gather.screen.base.personal.PersonalDataFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

public class ProfileFragment extends PersonalDataFragment<ProfileViewModel>
        implements BackPressFragment {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.toolbarProfile)
    Toolbar toolbar;
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;
    @BindView(R.id.tvSignOut)
    TextView tvSignOut;
    @BindView(R.id.tvAvatarPrompt)
    TextView tvAvatarPrompt;
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername;

    @Override
    protected ProfileViewModel createViewModel() {
        ImageSourceUseCase imageSourceUseCase = ViewModelProviders.of(getActivity())
                .get(GeneralProfileViewModel.class)
                .getImageSourceUseCase();
        ProfileViewModel profileViewModel = ViewModelProviders.of(this)
                .get(ProfileViewModel.class);
        profileViewModel.setImageSourceUseCase(imageSourceUseCase);
        return profileViewModel;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void subscribe() {
        super.subscribe();
        toolbar.setNavigationOnClickListener(navIcon -> getActivity().finish());
        viewModel.getUserData(this::onUserDataLoaded, this::onUserNotFound);
    }

    @Override
    public boolean handleBackPress() {
        if (viewModel.checkIfIsEditMode()) {
            changeUIMode(false);
            return true;
        }
        return false;
    }

    @OnClick(R.id.tvSignOut)
    public void onSignOutClick() {
        viewModel.signOut(() -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @OnClick(R.id.fabEdit)
    void onProfileModeClick() {
        viewModel.onProfileModeChanged(this::handleEditModeChange);
    }

    @Override
    protected void handleSuccess(AddDataStatus.Success success) {
        super.handleSuccess(success);
        getActivity().recreate();
    }

    private void handleEditModeChange(boolean isEditMode) {
        changeUIMode(isEditMode);
        if (!isEditMode) {
            super.onSubmitClick();
        }
    }

    private void changeUIMode(boolean isEditMode) {
        fabEdit.setImageResource(isEditMode ? R.drawable.ic_tick : R.drawable.ic_pencil);
        tvSignOut.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        ivAvatarPreview.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        tilUsername.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        lavTick.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        tvAvatarPrompt.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    private void onUserDataLoaded(FirebaseUser firebaseUser) {
        toolbar.setTitle(firebaseUser.getDisplayName());
        Glide.with(getContext())
                .load(firebaseUser.getPhotoUrl())
                .into(ivPhoto);
    }

    private void onUserNotFound() {
        snackbar(ivPhoto, "Couldn't load user data");
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
}
