package by.overpass.gather.ui.map.detail;

import android.location.Location;

import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hadilq.liveevent.LiveEvent;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.Map;

import javax.inject.Inject;

import by.overpass.gather.model.usecase.meeting.MeetingsUseCase;
import by.overpass.gather.model.usecase.permission.LocationPermissionUseCase;
import by.overpass.gather.ui.map.Meeting;

public class MapDetailViewModel extends BaseMapDetailViewModel {

    private static final String TAG = "MapDetailViewModel";

    private final MeetingsUseCase meetingsUseCase;
    private MarkersHelper markersHelper;

    @Inject
    public MapDetailViewModel(LiveEvent<Boolean> grantedLiveEvent,
                              MutableLiveData<FabAction> fabActionLiveData,
                              MutableLiveData<Location> locationLiveData,
                              MeetingsUseCase meetingsUseCase,
                              LocationPermissionUseCase locationPermissionUseCase) {
        super(grantedLiveEvent, locationLiveData, fabActionLiveData, locationPermissionUseCase);
        this.meetingsUseCase = meetingsUseCase;
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
