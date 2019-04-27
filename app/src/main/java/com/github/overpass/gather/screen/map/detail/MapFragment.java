package com.github.overpass.gather.screen.map.detail;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.model.commons.base.BackPressFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.usecase.permission.LocationPermissionUseCase;
import com.github.overpass.gather.screen.meeting.MeetingActivity;
import com.github.overpass.gather.screen.map.MapViewModel;
import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.model.repo.icon.IconRepo;
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
            onRatioReceived(ratio, marker);
        });
        return super.onMarkerClick(marker);
    }

    @Override
    protected boolean onInfoWindowClick(Marker marker) {
        viewModel.openMeeting(marker, id -> MeetingActivity.start(getContext(), id));
        return super.onInfoWindowClick(marker);
    }

    @Override
    protected void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        super.onStyleLoaded(style, mapboxMap);
        viewModel.setMarkersHelper(new MarkersHelper(map, new IconRepo(getContext())));
    }

    private void onRatioReceived(Current2MaxPeopleRatio ratio, Marker marker) {
        viewModel.updateSnippet(marker, ratio).observe(getViewLifecycleOwner(), updated -> {
            // probably do smth
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
