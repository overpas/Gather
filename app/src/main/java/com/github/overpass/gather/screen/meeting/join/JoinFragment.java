package com.github.overpass.gather.screen.meeting.join;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.DateFormatting;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.screen.base.BaseFragmentKt;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.chat.ChatFragment;
import com.github.overpass.gather.screen.meeting.enrolled.EnrolledActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

public class JoinFragment extends BaseFragmentKt<JoinViewModel> {

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
    @BindView(R.id.toolbarJoin)
    Toolbar toolbarJoin;
    @BindView(R.id.btnJoin)
    TextView btnJoin;

    @NotNull
    @Override
    protected JoinViewModel createViewModel() {
        return getViewModelProvider().get(JoinViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_join;
    }

    @Override
    protected void inject() {
        App.Companion.getComponentManager(this)
                .getJoinComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarJoin.setNavigationOnClickListener(navIcon -> requireActivity().finish());
        flProgress.setVisibility(View.VISIBLE);
        getViewModel().loadMeetingCheckEnrolled()
                .observe(getViewLifecycleOwner(), this::handleLoadStatus);
    }

    @OnClick(R.id.btnJoin)
    public void onJoinClick() {
        getViewModel().join().observe(getViewLifecycleOwner(), this::handleJoin);
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
        ProgressDialogFragment.Companion.show(getFragmentManager());
    }

    private void handleJoined(JoinStatus.Joined joined) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        FragmentUtils.replace(requireFragmentManager(), R.id.flMeetingContainer,
                ChatFragment.newInstance(), false);
    }

    private void handleJoinError(JoinStatus.Error error) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        snackbar(ivMeetingType, error.getThrowable().getLocalizedMessage());
    }

    private void handleEnrolled(JoinStatus.Enrolled enrolled) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        startActivity(new Intent(getContext(), EnrolledActivity.class));
        requireActivity().finish();
    }

    protected void handleLoadStatus(LoadMeetingStatus loadMeetingStatus) {
        switch (loadMeetingStatus.tag()) {
            case LoadPrivateMeetingStatus.ERROR:
                handleLoadError(loadMeetingStatus.as(LoadPrivateMeetingStatus.Error.class));
                break;
            case LoadPrivateMeetingStatus.SUCCESS:
                handleLoadSuccess(loadMeetingStatus.as(LoadPrivateMeetingStatus.Success.class));
                break;
            case LoadPrivateMeetingStatus.PROGRESS:
                handleProgress(loadMeetingStatus.as(LoadPrivateMeetingStatus.Progress.class));
                break;
        }
    }

    protected void handleLoadSuccess(LoadPrivateMeetingStatus.Success success) {
        flProgress.setVisibility(View.GONE);
        tvName.setText(success.getMeetingAndRatio().getMeeting().getName());
        tvPrivateMeeting.setVisibility(success.getMeetingAndRatio().getMeeting().isPrivate()
                ? View.VISIBLE : View.INVISIBLE);
        if (success.isAlreadyEnrolled()) {
            tvPrivateMeeting.setText(R.string.you_have_already_enrolled);
            btnJoin.setVisibility(View.GONE);
        }
        String ratio = String.format(Locale.getDefault(), "%d / %d",
                success.getMeetingAndRatio().getRatio().getCurrent(),
                success.getMeetingAndRatio().getRatio().getMax());
        tvRatio.setText(ratio);
        SimpleDateFormat format = DateFormatting.getMeetingDateFormat();
        tvDate.setText(format.format(success.getMeetingAndRatio().getMeeting().getDate()));
        if (MeetingType.isBusiness(success.getMeetingAndRatio().getMeeting().getType())) {
            ivMeetingType.setImageResource(R.drawable.ic_case_large);
        } else if (MeetingType.isProtest(success.getMeetingAndRatio().getMeeting().getType())) {
            ivMeetingType.setImageResource(R.drawable.ic_bund_large);
        } else {
            ivMeetingType.setImageResource(R.drawable.ic_beer_large);
        }
        getViewModel().getAddress(success.getMeetingAndRatio().getMeeting().getLongitude(),
                success.getMeetingAndRatio().getMeeting().getLatitude())
                .observe(getViewLifecycleOwner(), tvAddress::setText);
    }

    protected void handleProgress(LoadPrivateMeetingStatus.Progress progress) {
        flProgress.setVisibility(View.VISIBLE);
    }

    protected void handleLoadError(LoadPrivateMeetingStatus.Error error) {
        flProgress.setVisibility(View.GONE);
    }

    public static JoinFragment newInstance() {
        return new JoinFragment();
    }
}
