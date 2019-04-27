package com.github.overpass.gather.screen.map;

import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.permission.LocationPermissionUseCase;

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
