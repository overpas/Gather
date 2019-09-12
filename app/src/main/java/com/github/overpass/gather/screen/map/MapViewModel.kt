package com.github.overpass.gather.screen.map

import androidx.lifecycle.ViewModel

import com.github.overpass.gather.model.usecase.permission.LocationPermissionUseCase

import javax.inject.Inject

class MapViewModel @Inject constructor(
        val locationPermissionUseCase: LocationPermissionUseCase
) : ViewModel() {

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        locationPermissionUseCase.onRequestPermissionsResult(requestCode, permissions,
                grantResults)
    }
}
