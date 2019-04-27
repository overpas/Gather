package com.github.overpass.gather.screen.meeting.join;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

public class JoinFragment extends BaseMeetingFragment<JoinViewModel> {

    @BindView(R.id.flProgress)
    FrameLayout flProgress;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvRatio)
    TextView tvRatio;
    @BindView(R.id.tvPrivateMeeting)
    TextView tvPrivateMeeting;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.ivMeetingType)
    ImageView ivMeetingType;

    @Override
    protected JoinViewModel createViewModel() {
        return ViewModelProviders.of(this).get(JoinViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_join;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.loadMeeting(getMeetingId())
                .observe(getViewLifecycleOwner(), this::handleLoadStatus);
        flProgress.setVisibility(View.VISIBLE);
    }

    private void handleLoadStatus(LoadMeetingStatus loadMeetingStatus) {
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

    private void handleLoadSuccess(LoadMeetingStatus.Success success) {
        flProgress.setVisibility(View.GONE);
        tvName.setText(success.getMeetingAndRatio().getMeeting().getName());
//        tvAddress.setText(success.getMeetingAndRatio().getMeeting().getLatitude() + ", "
//                + success.getMeetingAndRatio().getMeeting().getLongitude());
        tvPrivateMeeting.setVisibility(success.getMeetingAndRatio().getMeeting().isPrivate()
                ? View.VISIBLE : View.INVISIBLE);
        String ratio = String.format(Locale.getDefault(), "%d / %d",
                success.getMeetingAndRatio().getRatio().getCurrent(),
                success.getMeetingAndRatio().getRatio().getMax());
        tvRatio.setText(ratio);
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        tvDate.setText(format.format(success.getMeetingAndRatio().getMeeting().getDate()));
        if (MeetingType.isBusiness(success.getMeetingAndRatio().getMeeting().getType())) {
            ivMeetingType.setImageResource(R.drawable.ic_case_large);
        } else if (MeetingType.isProtest(success.getMeetingAndRatio().getMeeting().getType())) {
            ivMeetingType.setImageResource(R.drawable.ic_bund_large);
        } else {
            ivMeetingType.setImageResource(R.drawable.ic_beer_large);
        }
        viewModel.getAddress(success.getMeetingAndRatio().getMeeting().getLatitude(),
                success.getMeetingAndRatio().getMeeting().getLongitude())
                .observe(getViewLifecycleOwner(), tvAddress::setText);
    }

    private void handleProgress(LoadMeetingStatus.Progress progress) {
        flProgress.setVisibility(View.VISIBLE);
    }

    private void handleLoadError(LoadMeetingStatus.Error error) {
        flProgress.setVisibility(View.GONE);
    }

    public static JoinFragment newInstance(String meetingId) {
        return newInstance(meetingId, new JoinFragment());
    }
}
