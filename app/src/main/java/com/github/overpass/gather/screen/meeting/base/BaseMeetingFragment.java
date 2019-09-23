package com.github.overpass.gather.screen.meeting.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.overpass.gather.screen.base.BaseFragmentKt;

public abstract class BaseMeetingFragment<VM extends BaseMeetingViewModel>
        extends BaseFragmentKt<VM> {

    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    protected String getMeetingId() {
        String defaultId = "-1";
        if (getArguments() != null) {
            String id = getArguments().getString(MEETING_ID_KEY, defaultId);
            if (id != null) {
                return id;
            }
        }
        return defaultId;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadMeetingData();
    }

    protected void onLoadMeetingData() {
        getViewModel().loadMeeting(getMeetingId())
                .observe(getViewLifecycleOwner(), this::handleLoadStatus);
    }

    protected void handleLoadStatus(LoadMeetingStatus loadMeetingStatus) {
        switch (loadMeetingStatus.tag()) {
            case LoadMeetingStatus.ERROR:
                handleLoadError(loadMeetingStatus.as(LoadMeetingStatus.Error.class));
                break;
            case LoadMeetingStatus.PROGRESS:
                handleProgress(loadMeetingStatus.as(LoadMeetingStatus.Progress.class));
                break;
            case LoadMeetingStatus.SUCCESS:
                handleLoadSuccess(loadMeetingStatus.as(LoadMeetingStatus.Success.class));
                break;
        }
    }

    protected void handleLoadSuccess(LoadMeetingStatus.Success success) {}

    protected void handleProgress(LoadMeetingStatus.Progress progress) {}

    protected void handleLoadError(LoadMeetingStatus.Error error) {}

    protected static <F extends BaseMeetingFragment> F newInstance(String meetingId, F fragment) {
        Bundle args = new Bundle();
        args.putString(MEETING_ID_KEY, meetingId);
        fragment.setArguments(args);
        return fragment;
    }
}
