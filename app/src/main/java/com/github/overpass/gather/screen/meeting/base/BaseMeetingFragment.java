package com.github.overpass.gather.screen.meeting.base;

import android.os.Bundle;

import com.github.overpass.gather.screen.base.BaseFragment;

public abstract class BaseMeetingFragment<VM extends BaseMeetingViewModel> extends BaseFragment<VM> {

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

    protected static <F extends BaseMeetingFragment> F newInstance(String meetingId, F fragment) {
        Bundle args = new Bundle();
        args.putString(MEETING_ID_KEY, meetingId);
        fragment.setArguments(args);
        return fragment;
    }
}
