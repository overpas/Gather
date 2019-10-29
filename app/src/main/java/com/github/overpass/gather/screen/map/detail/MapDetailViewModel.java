package com.github.overpass.gather.screen.map.detail;

import android.location.Location;

import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.overpass.gather.model.usecase.meeting.MeetingsUseCase;
import com.github.overpass.gather.model.usecase.permission.LocationPermissionUseCase;
import com.github.overpass.gather.screen.map.Meeting;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.Map;

import javax.inject.Inject;

public class MapDetailViewModel extends BaseMapDetailViewModel {

    private static final String TAG = "MapDetailViewModel";

    private final MeetingsUseCase meetingsUseCase;
    private MarkersHelper markersHelper;

    @Inject
    public MapDetailViewModel(MeetingsUseCase meetingsUseCase,
                              LocationPermissionUseCase locationPermissionUseCase) {
        super();
        this.meetingsUseCase = meetingsUseCase;
        setLocationPermissionUseCase(locationPermissionUseCase);
    }

    public LiveData<Map<String, Meeting>> scanArea(Location location) {
        return meetingsUseCase.getMeetings(location);
    }

    public LiveData<Current2MaxPeopleRatio> getCurrent2MaxPeopleRatio(String id) {
        return meetingsUseCase.getCurrent2MaxPeopleRatio(id);
    }

    public void getCurrent2MaxPeopleRatio(Marker marker,
                                          LifecycleOwner lifecycleOwner,
                                          Observer<Current2MaxPeopleRatio> observer) {
        String id = markersHelper.getIdByMarker(marker);
        if (!markersHelper.isRatioKnown(marker)) {
            getCurrent2MaxPeopleRatio(id).observe(lifecycleOwner, observer);
        }
    }

    public LiveData<Marker> updateSnippet(Marker marker, Current2MaxPeopleRatio ratio) {
        return markersHelper.updateSnippet(marker, ratio);
    }

    @Override
    public void setMarkersHelper(MarkersHelper markersHelper) {
        this.markersHelper = markersHelper;
    }

    public void replace(Map<String, Meeting> meetings) {
        markersHelper.replace(meetings);
    }

    public void openMeeting(Marker marker, Consumer<String> consumer) {
        consumer.accept(markersHelper.getIdByMarker(marker));
    }
}
