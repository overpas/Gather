package com.github.overpass.gather.map.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.map.LocationPermissionUseCase;
import com.github.overpass.gather.map.Meeting;
import com.github.overpass.gather.map.MeetingRepo;
import com.github.overpass.gather.map.MeetingsUseCase;
import com.github.overpass.gather.map.PermissionRequestResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BaseMapDetailViewModel extends ViewModel implements PermissionsListener,
        LocationEngineCallback<LocationEngineResult> {

    private static final String TAG = "BaseMapDetailViewModel";

    private final SingleLiveEvent<Boolean> permissionGrantedData;
    private final MutableLiveData<Location> locationData;
    private final PermissionsManager permissionsManager;
    private final MutableLiveData<MapDetailViewModel.FabAction> fabActionData;
    private LocationPermissionUseCase locationPermissionUseCase;
    private boolean hasShownLocationOnce;

    public BaseMapDetailViewModel() {
        permissionsManager = new PermissionsManager(this);
        permissionGrantedData = new SingleLiveEvent<>();
        locationData = new MutableLiveData<>();
        fabActionData = new MutableLiveData<>();
        fabActionData.setValue(MapDetailViewModel.FabAction.INIT);
    }

    LiveData<Boolean> getPermissionGrantedData() {
        return permissionGrantedData;
    }

    public LiveData<Location> getLocationData() {
        return locationData;
    }

    public LiveData<MapDetailViewModel.FabAction> getFabActionData() {
        return fabActionData;
    }

    void enableLocation(Style style, MapboxMap mapboxMap, Activity activity) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            doEnableLocation(activity, style, mapboxMap);
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
    void doEnableLocation(Activity activity, Style style, MapboxMap mapboxMap) {
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

    public void onFabClick() {
        if (MapDetailViewModel.FabAction.ADD_NEW == fabActionData.getValue()
                || MapDetailViewModel.FabAction.INIT == fabActionData.getValue()) {
            fabActionData.setValue(MapDetailViewModel.FabAction.CONFIRM_MARKER);
        } else {
            fabActionData.setValue(MapDetailViewModel.FabAction.ADD_NEW);
        }
    }

    public void resetFabAction() {
        fabActionData.setValue(MapDetailViewModel.FabAction.INIT);
    }

    public boolean hasShownLocationOnce() {
        return hasShownLocationOnce;
    }

    public void setHasShownLocationOnce(boolean hasShownLocationOnce) {
        this.hasShownLocationOnce = hasShownLocationOnce;
    }

    public void setMarkersHelper(MarkersHelper markersHelper) {
    }

    enum FabAction {
        INIT, ADD_NEW, CONFIRM_MARKER
    }
}
