package com.github.overpass.gather.map.detail;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

public class MarkersHelper {

    private List<Marker> data = new ArrayList<>();

    private final MapboxMap map;

    MarkersHelper(MapboxMap map) {
        this.map = map;
    }

    public void replace(List<MarkerOptions> markerOptions) {
        clear();
        data.addAll(map.addMarkers(markerOptions));
    }

    public void clear() {
        for (Marker marker: data) {
            map.removeMarker(marker);
        }
        data.clear();
    }
}
