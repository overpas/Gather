package com.github.overpass.gather.screen.meeting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.model.commons.base.BaseActivity;
import com.github.overpass.gather.screen.meeting.chat.ChatFragment;
import com.github.overpass.gather.screen.meeting.join.JoinFragment;

import butterknife.BindView;

public class MeetingActivity extends BaseActivity<MeetingViewModel> {

    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @BindView(R.id.lavProgress)
    LottieAnimationView lavProgress;

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
            lavProgress.setVisibility(View.VISIBLE);
            viewModel.isAllowed(getMeetingId()).observe(this, this::handleIsAllowed);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleIsAllowed(boolean isAllowed) {
        lavProgress.setVisibility(View.GONE);
        if (!isAllowed) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flMeetingContainer,
                    JoinFragment.newInstance(getMeetingId()), false);
        } else {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flMeetingContainer,
                    ChatFragment.newInstance(getMeetingId()), false);
        }
    }

    private String getMeetingId() {
        return getIntent().getExtras().getString(MEETING_ID_KEY);
    }

    public static void start(Context context, String meetingId) {
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.putExtra(MEETING_ID_KEY, meetingId);
        context.startActivity(intent);
    }
}
