package com.github.overpass.gather.map;

import android.os.Bundle;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

import com.github.overpass.gather.FragmentUtils;
import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseActivity;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class MapActivity extends BaseActivity<MapViewModel> implements MapFragment.BottomAppBarController {

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
                    MapFragment.newInstance(), false);
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
}
