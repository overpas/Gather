package by.overpass.gather.ui.map.detail

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.ui.base.BackPressFragment
import by.overpass.gather.ui.base.BaseFragmentKt
import by.overpass.gather.ui.create.NewMeetingActivity
import com.google.android.material.snackbar.Snackbar
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import kotlinx.android.synthetic.main.fragment_map.*


abstract class BaseMapFragment<VM : BaseMapDetailViewModel> : BaseFragmentKt<VM>(),
        BackPressFragment, OnMapReadyCallback, MapboxMap.OnMarkerClickListener {

    private lateinit var symbolManager: SymbolManager
    private lateinit var style: Style

    protected var map: MapboxMap? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.init()
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        on(viewModel.getPermissionGrantedData()) {
            onPermissionResponse(it)
        }
        on(viewModel.getLocationData()) {
            onLocationUpdated(it)
        }
        on(viewModel.getFabActionData()) {
            onFabActionChanged(it)
        }
        fab.setOnClickListener {
            viewModel.onFabClick()
        }
        fabMyLocation.setOnClickListener {
            if (viewModel.getLocationData().value != null) {
                onLocationUpdated(viewModel.getLocationData().value!!, true)
            }
        }
        ibBack.setOnClickListener {
            onBackClick()
        }
    }

    private fun onPermissionResponse(granted: Boolean) {
        if (!granted) {
            snackbar(R.string.no_location_permissions, Snackbar.LENGTH_SHORT)
        } else {
            viewModel.doEnableLocation(requireActivity(), style, map!!)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style -> onStyleLoaded(style, mapboxMap) }
    }

    protected open fun onLocationUpdated(location: Location, forceCameraMove: Boolean) {
        Log.d(TAG, "location: " + location.latitude + "; " + location.longitude)

        val position = CameraPosition.Builder()
                .target(LatLng(location.latitude, location.longitude))
                .zoom(17.0) // Sets the zoom
                .bearing(180.0) // Rotate the camera
                .build() // Creates a CameraPosition from the builder

        if (!viewModel.hasShownLocationOnce() || forceCameraMove) {
            map!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
            viewModel.setHasShownLocationOnce(true)
        }
    }

    /**
     * Called when the fragment is visible for the users.
     */
    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    /**
     * Called when the fragment is pausing.
     */
    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    /**
     * Called when the fragment state needs to be saved.
     *
     * @param outState The saved state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mapView != null && !mapView!!.isDestroyed) {
            mapView!!.onSaveInstanceState(outState)
        }
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null && !mapView!!.isDestroyed) {
            mapView!!.onLowMemory()
        }
    }

    /**
     * Called when the fragment is view hiearchy is being destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mapView!!.onDestroy()
    }

    protected open fun onStyleLoaded(style: Style, mapboxMap: MapboxMap) {
        this.style = style
        this.map = mapboxMap
        this.map!!.setOnMarkerClickListener(this)
        this.map!!.onInfoWindowClickListener = MapboxMap.OnInfoWindowClickListener {
            onInfoWindowClick(it)
        }
        symbolManager = SymbolManager(mapView, mapboxMap, style)
        mapboxMap.addOnMoveListener(object : MapVerticalFlingListener() {
            override fun onFlingUp() {
                Log.d(TAG, "onFlingUp: ")
                showBottomAppBar()
            }

            override fun onFlingDown() {
                Log.d(TAG, "onFlingDown: ")
                hideBottomAppBar()
            }
        })
        viewModel.enableLocation(style, mapboxMap, activity!!)
    }

    protected open fun onInfoWindowClick(marker: Marker): Boolean {
        return false
    }

    private fun onBackClick() {
        viewModel.resetFabAction()
    }

    override fun handleBackPress(): Boolean {
        return if (viewModel.getFabActionData().value === BaseMapDetailViewModel.FabAction.CONFIRM_MARKER) {
            onBackClick()
            true
        } else {
            false
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    private fun hideBottomAppBar() {
        bottomAppBar.animate()
                .translationY(bottomAppBar.minimumHeight.toFloat())
                .setInterpolator(DecelerateInterpolator())
                .setDuration(600)
                .start()
    }

    private fun showBottomAppBar() {
        bottomAppBar.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(600)
                .start()
    }

    private fun onFabActionChanged(fabAction: BaseMapDetailViewModel.FabAction) {
        when (fabAction) {
            BaseMapDetailViewModel.FabAction.ADD_NEW -> {
                switchMarker(false)
                if (map != null) {
                    NewMeetingActivity.start(map!!.cameraPosition.target, context!!)
                }
            }
            BaseMapDetailViewModel.FabAction.CONFIRM_MARKER -> switchMarker(true)
            else -> switchMarker(false)
        }
    }

    private fun switchMarker(confirmMarker: Boolean) {
        if (confirmMarker) {
            fab.setImageResource(R.drawable.ic_tick)
            ivCenterMarker.visibility = View.VISIBLE
            ibBack.visibility = View.VISIBLE
        } else {
            ivCenterMarker.visibility = View.GONE
            fab.setImageResource(R.drawable.ic_add_marker)
            ibBack.visibility = View.GONE
        }
    }

    private fun onLocationUpdated(location: Location) {
        onLocationUpdated(location, false)
    }

    companion object {

        private const val TAG = "BaseMapFragment"
    }
}
