package com.github.overpass.gather.screen.meeting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.model.commons.base.BaseActivity;
import com.github.overpass.gather.screen.meeting.join.JoinFragment;

public class MeetingActivity extends BaseActivity<MeetingViewModel> {

    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meeting;
    }

    @Override
    protected MeetingViewModel createViewModel() {
        return ViewModelProviders.of(this).get(MeetingViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            String meetingId = getIntent().getExtras().getString(MEETING_ID_KEY);
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flChatContainer,
                    JoinFragment.newInstance(meetingId), false);
        }
    }

    public static void start(Context context, String meetingId) {
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.putExtra(MEETING_ID_KEY, meetingId);
        context.startActivity(intent);
    }
}
