package com.github.overpass.gather.map;

import android.content.Context;
import android.util.Log;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import androidx.annotation.NonNull;

public class MapFragment extends SupportMapFragment {

    private static final String TAG = "MapFragment";

    private BottomAppBarController bottomAppBarController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomAppBarController) {
            bottomAppBarController = (BottomAppBarController) context;
        } else {
            throw new RuntimeException(context + " must implement "
                    + BottomAppBarController.class.getSimpleName());
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        super.onMapReady(mapboxMap);
        mapboxMap.setStyle(Style.OUTDOORS, style -> {
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
            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomAppBarController = null;
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public interface BottomAppBarController {
        void hideBottomAppBar();

        void showBottomAppBar();
    }
}
