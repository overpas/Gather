package com.github.overpass.gather.map;

import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private final LocationPermissionUseCase locationPermissionUseCase;

    public MapViewModel() {
        this.locationPermissionUseCase = new LocationPermissionUseCase();
    }

    public LocationPermissionUseCase getLocationPermissionUseCase() {
        return locationPermissionUseCase;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        locationPermissionUseCase.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }
}
