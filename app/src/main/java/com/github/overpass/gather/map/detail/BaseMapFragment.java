package com.github.overpass.gather.map.detail;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.overpass.gather.BackPressFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseFragment;
import com.github.overpass.gather.create.NewMeetingActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;

public abstract class BaseMapFragment<VM extends BaseMapDetailViewModel> extends BaseFragment<VM>
        implements BackPressFragment, OnMapReadyCallback, MapboxMap.OnMarkerClickListener {

    private static final String TAG = "BaseMapFragment";

    protected SymbolManager symbolManager;
    protected MapboxMap map;
    protected Style style;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.ivCenterMarker)
    ImageView ivCenterMarker;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        viewModel.getPermissionGrantedData()
                .observe(getViewLifecycleOwner(), this::onPermissionResponse);
        viewModel.getLocationData()
                .observe(getViewLifecycleOwner(), this::onLocationUpdated);
        viewModel.getFabActionData()
                .observe(getViewLifecycleOwner(), this::onFabActionChanged);
    }

    protected void onPermissionResponse(boolean granted) {
        if (!granted) {
            snackbar(getView(), getString(R.string.no_location_permissions));
        } else {
            viewModel.doEnableLocation(getActivity(), style, map);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        viewModel.onFabClick();
    }

    @OnClick(R.id.fabMyLocation)
    public void onMyLocationClick() {
        if (viewModel.getLocationData().getValue() != null) {
            onLocationUpdated(viewModel.getLocationData().getValue(), true);
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> onStyleLoaded(style, mapboxMap));
    }

    protected void onLocationUpdated(Location location, boolean forceCameraMove) {
        Log.d(TAG, "location: " + location.getLatitude() + "; " + location.getLongitude());

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .build(); // Creates a CameraPosition from the builder

        if (!viewModel.hasShownLocationOnce() || forceCameraMove) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            viewModel.setHasShownLocationOnce(true);
        }
    }

    /**
     * Called when the fragment is visible for the users.
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Called when the fragment is pausing.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Called when the fragment state needs to be saved.
     *
     * @param outState The saved state
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onSaveInstanceState(outState);
        }
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onLowMemory();
        }
    }

    /**
     * Called when the fragment is view hiearchy is being destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    protected void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        this.style = style;
        this.map = mapboxMap;
        this.map.setOnMarkerClickListener(this);
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        mapboxMap.addOnMoveListener(new MapVerticalFlingListener() {
            @Override
            public void onFlingUp() {
                Log.d(TAG, "onFlingUp: ");
                showBottomAppBar();
            }

            @Override
            public void onFlingDown() {
                Log.d(TAG, "onFlingDown: ");
                hideBottomAppBar();
            }
        });
        viewModel.enableLocation(style, mapboxMap, getActivity());
    }

    @OnClick(R.id.ibBack)
    public void onBackClick() {
        viewModel.resetFabAction();
    }

    @Override
    public boolean handleBackPress() {
        if (viewModel.getFabActionData().getValue()
                == MapDetailViewModel.FabAction.CONFIRM_MARKER) {
            onBackClick();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    private void hideBottomAppBar() {
        bottomAppBar.animate()
                .translationY(bottomAppBar.getMinimumHeight())
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    private void showBottomAppBar() {
        bottomAppBar.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    private void onFabActionChanged(MapDetailViewModel.FabAction fabAction) {
        switch (fabAction) {
            case ADD_NEW:
                switchMarker(false);
                NewMeetingActivity.start(map.getCameraPosition().target, getContext());
                break;
            case CONFIRM_MARKER:
                switchMarker(true);
                break;
            default:
                switchMarker(false);
        }
    }

    private void switchMarker(boolean confirmMarker) {
        if (confirmMarker) {
            fab.setImageResource(R.drawable.ic_tick);
            ivCenterMarker.setVisibility(View.VISIBLE);
            ibBack.setVisibility(View.VISIBLE);
        } else {
            ivCenterMarker.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_add_marker);
            ibBack.setVisibility(View.GONE);
        }
    }

    private void onLocationUpdated(Location location) {
        onLocationUpdated(location, false);
    }
}
