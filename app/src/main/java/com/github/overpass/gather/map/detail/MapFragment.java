package com.github.overpass.gather.map.detail;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.BackPressFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.map.LocationPermissionUseCase;
import com.github.overpass.gather.map.MapViewModel;
import com.github.overpass.gather.map.Meeting;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.Map;

public class MapFragment extends BaseMapFragment<MapDetailViewModel>
        implements BackPressFragment, OnMapReadyCallback {

    private static final String TAG = "MainMapFragment";

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
    protected void onLocationUpdated(Location location, boolean forceCameraMove) {
        super.onLocationUpdated(location, forceCameraMove);
        viewModel.scanArea(location)
                .observe(getViewLifecycleOwner(), this::onMeetingsLoaded);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        viewModel.getCurrent2MaxPeopleRatio(marker, getViewLifecycleOwner(), ratio -> {
            onRatioRecieved(ratio, marker);
        });
        return super.onMarkerClick(marker);
    }

    @Override
    protected void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        super.onStyleLoaded(style, mapboxMap);
        viewModel.setMarkersHelper(new MarkersHelper(map, new IconRepo(getContext())));
    }

    private void onRatioRecieved(Current2MaxPeopleRatio ratio, Marker marker) {
        viewModel.updateSnippet(marker, ratio).observe(getViewLifecycleOwner(), updated -> {
            updated.showInfoWindow(map, mapView);
        });
    }

    private void onMeetingsLoaded(Map<String, Meeting> meetings) {
        Log.d(TAG, "onMeetingsLoaded: " + meetings.toString());
        viewModel.replace(meetings);
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }
}
