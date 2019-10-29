package by.overpass.gather.ui.map.detail;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import by.overpass.gather.R;
import by.overpass.gather.ui.base.BackPressFragment;
import by.overpass.gather.ui.base.BaseFragmentKt;
import by.overpass.gather.ui.create.NewMeetingActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;

import butterknife.BindView;
import butterknife.OnClick;
import by.overpass.gather.ui.base.BaseFragmentKt;
import by.overpass.gather.ui.create.NewMeetingActivity;

import static by.overpass.gather.commons.android.SnackbarKt.snackbar;


public abstract class BaseMapFragment<VM extends BaseMapDetailViewModel> extends BaseFragmentKt<VM>
        implements BackPressFragment, OnMapReadyCallback, MapboxMap.OnMarkerClickListener {

    private static final String TAG = "BaseMapFragment";

    protected SymbolManager symbolManager;
    protected MapboxMap map;
    protected Style style;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.bottomAppBar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.ivCenterMarker)
    ImageView ivCenterMarker;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        getViewModel().getPermissionGrantedData()
                .observe(getViewLifecycleOwner(), this::onPermissionResponse);
        getViewModel().getLocationData()
                .observe(getViewLifecycleOwner(), this::onLocationUpdated);
        getViewModel().getFabActionData()
                .observe(getViewLifecycleOwner(), this::onFabActionChanged);
    }

    protected void onPermissionResponse(boolean granted) {
        if (!granted) {
            snackbar(this, R.string.no_location_permissions, Snackbar.LENGTH_SHORT);
        } else {
            getViewModel().doEnableLocation(getActivity(), style, map);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        getViewModel().onFabClick();
    }

    @OnClick(R.id.fabMyLocation)
    public void onMyLocationClick() {
        if (getViewModel().getLocationData().getValue() != null) {
            onLocationUpdated(getViewModel().getLocationData().getValue(), true);
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> onStyleLoaded(style, mapboxMap));
    }

    protected void onLocationUpdated(Location location, boolean forceCameraMove) {
        Log.d(TAG, "location: " + location.getLatitude() + "; " + location.getLongitude());

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .build(); // Creates a CameraPosition from the builder

        if (!getViewModel().hasShownLocationOnce() || forceCameraMove) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            getViewModel().setHasShownLocationOnce(true);
        }
    }

    /**
     * Called when the fragment is visible for the users.
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Called when the fragment is pausing.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Called when the fragment state needs to be saved.
     *
     * @param outState The saved state
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onSaveInstanceState(outState);
        }
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onLowMemory();
        }
    }

    /**
     * Called when the fragment is view hiearchy is being destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    protected void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        this.style = style;
        this.map = mapboxMap;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnInfoWindowClickListener(this::onInfoWindowClick);
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        mapboxMap.addOnMoveListener(new MapVerticalFlingListener() {
            @Override
            public void onFlingUp() {
                Log.d(TAG, "onFlingUp: ");
                showBottomAppBar();
            }

            @Override
            public void onFlingDown() {
                Log.d(TAG, "onFlingDown: ");
                hideBottomAppBar();
            }
        });
        getViewModel().enableLocation(style, mapboxMap, getActivity());
    }

    protected boolean onInfoWindowClick(Marker marker) {
        return false;
    }

    @OnClick(R.id.ibBack)
    public void onBackClick() {
        getViewModel().resetFabAction();
    }

    @Override
    public boolean handleBackPress() {
        if (getViewModel().getFabActionData().getValue()
                == MapDetailViewModel.FabAction.CONFIRM_MARKER) {
            onBackClick();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    private void hideBottomAppBar() {
        bottomAppBar.animate()
                .translationY(bottomAppBar.getMinimumHeight())
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    private void showBottomAppBar() {
        bottomAppBar.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
    }

    private void onFabActionChanged(MapDetailViewModel.FabAction fabAction) {
        switch (fabAction) {
            case ADD_NEW:
                switchMarker(false);
                if (map != null) {
                    NewMeetingActivity.Companion.start(map.getCameraPosition().target, getContext());
                }
                break;
            case CONFIRM_MARKER:
                switchMarker(true);
                break;
            default:
                switchMarker(false);
        }
    }

    private void switchMarker(boolean confirmMarker) {
        if (confirmMarker) {
            fab.setImageResource(R.drawable.ic_tick);
            ivCenterMarker.setVisibility(View.VISIBLE);
            ibBack.setVisibility(View.VISIBLE);
        } else {
            ivCenterMarker.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_add_marker);
            ibBack.setVisibility(View.GONE);
        }
    }

    private void onLocationUpdated(Location location) {
        onLocationUpdated(location, false);
    }
}
