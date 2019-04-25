package com.github.overpass.gather.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.annimon.stream.Stream;
import com.github.overpass.gather.BackPressFragment;
import com.github.overpass.gather.FragmentUtils;
import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseActivity;
import com.github.overpass.gather.map.detail.MapFragment;

public class MapActivity extends BaseActivity<MapViewModel> {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map;
    }

    @Override
    protected MapViewModel createViewModel() {
        return ViewModelProviders.of(this).get(MapViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flMapFragmentContainer,
                    MapFragment.newInstance(), false);
        }
    }

    @Override
    public void onBackPressed() {
        if (!Stream.of(getSupportFragmentManager().getFragments())
                .filter(fragment -> fragment instanceof BackPressFragment)
                .map(fragment -> (BackPressFragment) fragment)
                .anyMatch(BackPressFragment::handleBackPress)) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
