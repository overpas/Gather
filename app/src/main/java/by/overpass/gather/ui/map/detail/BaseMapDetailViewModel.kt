package by.overpass.gather.ui.map.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.model.usecase.permission.LocationPermissionUseCase
import by.overpass.gather.ui.map.PermissionRequestResult
import com.hadilq.liveevent.LiveEvent
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

abstract class BaseMapDetailViewModel(
        private val permissionGrantedData: LiveEvent<Boolean>,
        private val locationData: MutableLiveData<Location>,
        private val fabActionsData: MutableLiveData<FabAction>,
        private var locationPermissionUseCase: LocationPermissionUseCase
) : ViewModel(), PermissionsListener, LocationEngineCallback<LocationEngineResult> {

    private var hasShownLocationOnce: Boolean = false
    private val permissionsManager: PermissionsManager get() = PermissionsManager(this)

    fun init() {
        fabActionsData.value = FabAction.INIT
        locationPermissionUseCase.permissionRequestResultData.observeForever {
            this.onRequestPermissionsResult(it)
        }
    }

    fun getPermissionGrantedData(): LiveData<Boolean> {
        return permissionGrantedData
    }

    fun getLocationData(): LiveData<Location> {
        return locationData
    }

    fun getFabActionData(): LiveData<FabAction> {
        return fabActionsData
    }

    fun enableLocation(style: Style, mapboxMap: MapboxMap, activity: Activity) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            doEnableLocation(activity, style, mapboxMap)
        } else {
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
    }

    override fun onPermissionResult(granted: Boolean) {
        permissionGrantedData.value = granted
    }

    @SuppressLint("MissingPermission")
    fun doEnableLocation(activity: Activity, style: Style, mapboxMap: MapboxMap) {
        // Get an instance of the component
        val locationComponent = mapboxMap.locationComponent
        // Activate with options
        locationComponent.activateLocationComponent(activity, style)
        // Enable to make component visible
        locationComponent.isLocationComponentEnabled = true
        // Set the component's camera mode
        locationComponent.cameraMode = CameraMode.TRACKING
        // Set the component's render mode
        locationComponent.renderMode = RenderMode.COMPASS
        requestLocationUpdates(locationComponent)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(locationComponent: LocationComponent) {
        val locationRequest = LocationEngineRequest.Builder(15000)
                .build()
        val locationEngine = locationComponent.locationEngine
        locationEngine?.requestLocationUpdates(locationRequest, this, null)
    }

    private fun onRequestPermissionsResult(requestResult: PermissionRequestResult) {
        permissionsManager.onRequestPermissionsResult(requestResult.requestCode,
                requestResult.permissions, requestResult.grantResults)
    }

    override fun onSuccess(result: LocationEngineResult) {
        locationData.value = result.lastLocation
    }

    override fun onFailure(exception: Exception) {
    }

    fun onFabClick() {
        if (FabAction.ADD_NEW == fabActionsData.value || FabAction.INIT == fabActionsData.value) {
            fabActionsData.setValue(FabAction.CONFIRM_MARKER)
        } else {
            fabActionsData.setValue(FabAction.ADD_NEW)
        }
    }

    fun resetFabAction() {
        fabActionsData.value = FabAction.INIT
    }

    fun hasShownLocationOnce(): Boolean {
        return hasShownLocationOnce
    }

    fun setHasShownLocationOnce(hasShownLocationOnce: Boolean) {
        this.hasShownLocationOnce = hasShownLocationOnce
    }

    open fun setMarkersHelper(markersHelper: MarkersHelper) {}

    enum class FabAction {
        INIT, ADD_NEW, CONFIRM_MARKER
    }
}
