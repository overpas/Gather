package com.github.overpass.gather.screen.profile;

import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.base.BaseFragment;
import com.github.overpass.gather.screen.auth.login.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

public class ProfileFragment extends BaseFragment<ProfileViewModel> {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.toolbarProfile)
    Toolbar toolbar;

    @Override
    protected ProfileViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ProfileViewModel.class);
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

    @OnClick(R.id.tvSignOut)
    public void onSignOutClick() {
        viewModel.signOut(() -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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
