package com.github.overpass.gather.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import com.github.overpass.gather.SingleLiveEvent;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainMapViewModel extends ViewModel implements PermissionsListener, LocationEngineCallback<LocationEngineResult> {

    private static final String TAG = "MainMapViewModel";

    private final SingleLiveEvent<Boolean> permissionGrantedData;
    private final MutableLiveData<Location> locationData;
    private final PermissionsManager permissionsManager;
    private LocationPermissionUseCase locationPermissionUseCase;
    private Style style;
    private MapboxMap mapboxMap;

    public MainMapViewModel() {
        permissionsManager = new PermissionsManager(this);
        permissionGrantedData = new SingleLiveEvent<>();
        locationData = new MutableLiveData<>();
    }

    SingleLiveEvent<Boolean> getPermissionGrantedData() {
        return permissionGrantedData;
    }

    public MutableLiveData<Location> getLocationData() {
        return locationData;
    }

    public Style getStyle() {
        return style;
    }

    void enableLocation(Style style, MapboxMap mapboxMap, Activity activity) {
        this.style = style;
        this.mapboxMap = mapboxMap;
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            enableLocation(activity);
        } else {
            permissionsManager.requestLocationPermissions(activity);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        permissionGrantedData.setValue(granted);
    }

    @SuppressLint("MissingPermission")
    void enableLocation(Activity activity) {
        if (mapboxMap == null || style == null) {
            return;
        }
        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        // Activate with options
        locationComponent.activateLocationComponent(activity, style);
        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);
        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);
        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);
        requestLocationUpdates(locationComponent);
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates(LocationComponent locationComponent) {
        LocationEngineRequest locationRequest = new LocationEngineRequest.Builder(15000)
                .build();
        LocationEngine locationEngine = locationComponent.getLocationEngine();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates(locationRequest, this, null);
        }
    }

    void setLocationPermissionUseCase(LocationPermissionUseCase locationPermissionUseCase) {
        this.locationPermissionUseCase = locationPermissionUseCase;
        this.locationPermissionUseCase.getPermissionRequestResultData()
                .observeForever(this::onRequestPermissionsResult);
    }

    private void onRequestPermissionsResult(PermissionRequestResult requestResult) {
        permissionsManager.onRequestPermissionsResult(requestResult.getRequestCode(),
                requestResult.getPermissions(), requestResult.getGrantResults());
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        locationData.setValue(result.getLastLocation());
    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }
}
