package com.github.overpass.gather.screen.profile;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.screen.base.BackPressActivity;
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment;

public class ProfileActivity extends BackPressActivity<GeneralProfileViewModel>
        implements PickImageDialogFragment.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flProfileContainer,
                    ProfileFragment.newInstance(), false);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_profile;
    }

    @Override
    protected GeneralProfileViewModel createViewModel() {
        return ViewModelProviders.of(this).get(GeneralProfileViewModel.class);
    }

    @Override
    public void onGallery() {
        viewModel.onGallery();
    }

    @Override
    public void onCamera() {
        viewModel.onCamera();
    }
}
