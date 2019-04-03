package com.github.overpass.gather.map.detail;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.github.overpass.gather.BackPressFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseFragment;
import com.github.overpass.gather.create.NewMeetingActivity;
import com.github.overpass.gather.map.LocationPermissionUseCase;
import com.github.overpass.gather.map.MapViewModel;
import com.github.overpass.gather.map.Meeting;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;

public class MapFragment extends BaseFragment<MapDetailViewModel>
        implements BackPressFragment, OnMapReadyCallback {

    private static final String TAG = "MainMapFragment";

    private MarkersHelper markersHelper;
    private SymbolManager symbolManager;
    private MapboxMap map;

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
    protected MapDetailViewModel createViewModel() {
        LocationPermissionUseCase locationPermissionUseCase = ViewModelProviders.of(getActivity())
                .get(MapViewModel.class)
                .getLocationPermissionUseCase();
        MapDetailViewModel viewModel = ViewModelProviders.of(this)
                .get(MapDetailViewModel.class);
        viewModel.setLocationPermissionUseCase(locationPermissionUseCase);
        return viewModel;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map;
    }

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

    private void onLocationUpdated(Location location) {
        onLocationUpdated(location, false);
    }

    private void onLocationUpdated(Location location, boolean forceCameraMove) {
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
        viewModel.scanArea(location)
                .observe(getViewLifecycleOwner(), this::onMeetingsLoaded);
    }

    private void onMeetingsLoaded(List<Meeting> meetings) {
        Log.d(TAG, "onMeetingsLoaded: " + meetings.toString());
        markersHelper.replace(Stream.of(meetings)
                .map(m -> new MarkerOptions()
                        .position(new LatLng(m.getLatitude(), m.getLongitude()))
                        .title(m.getName()))
                .toList());
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

    @OnClick(R.id.ibBack)
    public void onBackClick() {
        viewModel.resetFabAction();
    }

    private void onPermissionResponse(boolean granted) {
        if (!granted) {
            snackbar(getView(), getString(R.string.no_location_permissions));
        } else {
            viewModel.enableLocation(getActivity());
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> onStyleLoaded(style, mapboxMap));
    }

    private void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        this.map = mapboxMap;
        this.markersHelper = new MarkersHelper(map);
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

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public boolean handleBackPress() {
        if (viewModel.getFabActionData().getValue() == MapDetailViewModel.FabAction.CONFIRM_MARKER) {
            onBackClick();
            return true;
        } else {
            return false;
        }
    }
}
