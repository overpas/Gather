package com.github.overpass.gather.map;

import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import com.github.overpass.gather.FragmentUtils;
import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseActivity;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class MapActivity extends BaseActivity<MapViewModel>
        implements MainMapFragment.BottomAppBarController {

    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;

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
                    MainMapFragment.newInstance(), false);
        }
    }

    @Override
    public void hideBottomAppBar() {
        bottomAppBar.animate()
                .translationY(bottomAppBar.getMinimumHeight())
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    @Override
    public void showBottomAppBar() {
        bottomAppBar.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
