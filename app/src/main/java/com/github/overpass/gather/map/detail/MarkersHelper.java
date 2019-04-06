package com.github.overpass.gather.map.detail;

import android.text.TextUtils;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.map.Meeting;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class MarkersHelper {

    private Map<String, Marker> data = new HashMap<>();

    private final MapboxMap map;
    private final IconRepo iconRepo;

    MarkersHelper(MapboxMap map, IconRepo iconRepo) {
        this.map = map;
        this.iconRepo = iconRepo;
    }

    void replace(Map<String, Meeting> meetings) {
        clear();
        Map<String, MarkerOptions> markerMappings = meetingsToMarkerOptions(meetings);
        for (Map.Entry<String, MarkerOptions> entry : markerMappings.entrySet()) {
            data.put(entry.getKey(), map.addMarker(entry.getValue()));
        }
    }

    @Nullable
    String getIdByMarker(Marker marker) {
        for (Map.Entry<String, Marker> entry : data.entrySet()) {
            if (entry.getValue().equals(marker)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void clear() {
        for (Map.Entry<String, Marker> entry : data.entrySet()) {
            map.removeMarker(entry.getValue());
        }
        data.clear();
    }

    private MarkerOptions meetingToMarkerOptions(Meeting meeting) {
        return new MarkerOptions()
                .position(new LatLng(meeting.getLatitude(),
                        meeting.getLongitude()))
                .setIcon(iconRepo.getByType(meeting.getType()))
                .title(meeting.getName());
    }

    private Map<String, MarkerOptions> meetingsToMarkerOptions(Map<String, Meeting> meetings) {
        Map<String, MarkerOptions> map = new HashMap<>();
        for (Map.Entry<String, Meeting> entry : meetings.entrySet()) {
            map.put(entry.getKey(), meetingToMarkerOptions(entry.getValue()));
        }
        return map;
    }

    public LiveData<Marker> updateSnippet(Marker marker, Current2MaxPeopleRatio ratio) {
        SingleLiveEvent<Marker> markerUpdateStatus = new SingleLiveEvent<>();
        for (Map.Entry<String, Marker> entry : data.entrySet()) {
            if (entry.getValue().equals(marker)) {
                entry.getValue().setSnippet(ratio.getCurrent() + " / " + ratio.getMax());
                markerUpdateStatus.setValue(entry.getValue());
            }
        }
        return markerUpdateStatus;
    }

    public boolean isRatioKnown(Marker marker) {
        for (Map.Entry<String, Marker> entry : data.entrySet()) {
            if (entry.getValue().equals(marker)
                    && !TextUtils.isEmpty(entry.getValue().getSnippet())) {
                return true;
            }
        }
        return false;
    }
}
