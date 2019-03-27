package com.github.overpass.gather.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.overpass.gather.R;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import static com.github.overpass.gather.UIUtil.snackbar;

public class MainMapFragment extends SupportMapFragment<MainMapViewModel> {

    private static final String TAG = "MainMapFragment";

    private BottomAppBarController bottomAppBarController;
    private SymbolManager symbolManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomAppBarController) {
            bottomAppBarController = (BottomAppBarController) context;
        } else {
            throw new RuntimeException(context + " must implement "
                    + BottomAppBarController.class.getSimpleName());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel();
        viewModel.getPermissionGrantedData()
                .observe(getViewLifecycleOwner(), this::onPermissionResponse);
        viewModel.getLocationData()
                .observe(getViewLifecycleOwner(), this::onLocationUpdated);
    }

    private void onLocationUpdated(Location location) {
        Log.d(TAG, "location: " + location.getLatitude() + "; " + location.getLongitude());
        SymbolOptions options = new SymbolOptions();
        options.withLatLng(new LatLng(location));
        options.setDraggable(false);
        options.withIconImage("airport");
        options.withTextField("I'm here");
        symbolManager.create(options);

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the new camera position
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .build(); // Creates a CameraPosition from the builder
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    protected MainMapViewModel createViewModel() {
        LocationPermissionUseCase locationPermissionUseCase = ViewModelProviders.of(getActivity())
                .get(MapViewModel.class)
                .getLocationPermissionUseCase();
        MainMapViewModel viewModel = ViewModelProviders.of(this).get(MainMapViewModel.class);
        viewModel.setLocationPermissionUseCase(locationPermissionUseCase);
        return viewModel;
    }

    private void onPermissionResponse(boolean granted) {
        if (!granted) {
            snackbar(getView(), getString(R.string.no_location_permissions));
        } else {
            viewModel.enableLocation(getActivity());
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        super.onMapReady(mapboxMap);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> onStyleLoaded(style, mapboxMap));
    }

    private void onStyleLoaded(Style style, MapboxMap mapboxMap) {
        symbolManager = new SymbolManager(map, mapboxMap, style);
        mapboxMap.addOnMoveListener(new MapVerticalFlingListener() {
            @Override
            public void onFlingUp() {
                Log.d(TAG, "onFlingUp: ");
                bottomAppBarController.showBottomAppBar();
            }

            @Override
            public void onFlingDown() {
                Log.d(TAG, "onFlingDown: ");
                bottomAppBarController.hideBottomAppBar();
            }
        });
        viewModel.enableLocation(style, mapboxMap, getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomAppBarController = null;
    }

    public static MainMapFragment newInstance() {
        return new MainMapFragment();
    }

    public interface BottomAppBarController {

        void hideBottomAppBar();

        void showBottomAppBar();
    }
}
