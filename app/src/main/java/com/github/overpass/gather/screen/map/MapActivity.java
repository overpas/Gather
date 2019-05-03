package com.github.overpass.gather.screen.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.screen.base.BackPressActivity;
import com.github.overpass.gather.screen.map.detail.MapFragment;

public class MapActivity extends BackPressActivity<MapViewModel> {

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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
