package by.overpass.gather.ui.map

import androidx.lifecycle.ViewModel

import by.overpass.gather.model.usecase.permission.LocationPermissionUseCase

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
