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
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.chat.ChatFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

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

    @OnClick(R.id.btnJoin)
    public void onJoinClick() {
        viewModel.join(getMeetingId()).observe(getViewLifecycleOwner(), this::handleJoin);
    }

    private void handleJoin(JoinStatus status) {
        switch (status.tag()) {
            case JoinStatus.ENROLLED:
                handleEnrolled(status.as(JoinStatus.Enrolled.class));
                break;
            case JoinStatus.ERROR:
                handleJoinError(status.as(JoinStatus.Error.class));
                break;
            case JoinStatus.JOINED:
                handleJoined(status.as(JoinStatus.Joined.class));
                break;
            case JoinStatus.PROGRESS:
                handleProgress(status.as(JoinStatus.Progress.class));
                break;
        }
    }

    private void handleProgress(JoinStatus.Progress progress) {
        ProgressDialogFragment.show(getFragmentManager());
    }

    private void handleJoined(JoinStatus.Joined joined) {
        ProgressDialogFragment.show(getFragmentManager());
        FragmentUtils.replace(getFragmentManager(), R.id.flMeetingContainer,
                ChatFragment.newInstance(getMeetingId()), false);
    }

    private void handleJoinError(JoinStatus.Error error) {
        ProgressDialogFragment.show(getFragmentManager());
        snackbar(ivMeetingType, error.getThrowable().getLocalizedMessage());
    }

    private void handleEnrolled(JoinStatus.Enrolled enrolled) {
        ProgressDialogFragment.show(getFragmentManager());
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
        viewModel.getAddress(success.getMeetingAndRatio().getMeeting().getLongitude(),
                success.getMeetingAndRatio().getMeeting().getLatitude())
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
