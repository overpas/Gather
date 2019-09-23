package com.github.overpass.gather.screen.dialog.details;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.DateFormatting;
import com.github.overpass.gather.model.commons.Fragments;
import com.github.overpass.gather.screen.base.BaseDialogFragment;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class MeetingDetailsDialogFragment extends BaseDialogFragment<MeetingDetailsViewModel> {

    private static final String TAG = "MeetingDetailsDialogFra";
    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    private TextView tvAddress;
    private TextView tvRatio;
    private TextView tvDate;
    private LottieAnimationView lavProgress;

    @Override
    protected void inject() {
        App.Companion.getComponentManager(this)
                .getMeetingDetailsComponent()
                .inject(this);
    }

    @NotNull
    @Override
    protected MeetingDetailsViewModel createViewModel() {
        return getViewModelProvider().get(MeetingDetailsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_dialog_meeting_details, null);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvRatio = view.findViewById(R.id.tvRatio);
        tvDate = view.findViewById(R.id.tvDate);
        lavProgress = view.findViewById(R.id.lavProgress);
        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    @Override
    public void onBind() {
        super.onBind();
        viewModel.loadMeeting(getMeetingId()).observe(this, this::handleMeeting);
    }

    private void handleMeeting(LoadMeetingStatus loadMeetingStatus) {
        switch (loadMeetingStatus.tag()) {
            case LoadMeetingStatus.ERROR:
                handleError(loadMeetingStatus.as(LoadMeetingStatus.Error.class));
                break;
            case LoadMeetingStatus.SUCCESS:
                handleSuccess(loadMeetingStatus.as(LoadMeetingStatus.Success.class));
                break;
            case LoadMeetingStatus.PROGRESS:
                handleProgress();
                break;
        }
    }

    private void handleError(LoadMeetingStatus.Error error) {
        lavProgress.setVisibility(View.GONE);
        toast(this, getString(R.string.couldnt_load_data));
        hide(getFragmentManager());
    }

    private void handleSuccess(LoadMeetingStatus.Success success) {
        lavProgress.setVisibility(View.GONE);
        SimpleDateFormat format = DateFormatting.getMeetingDateFormat();
        tvDate.setText(format.format(success.getMeetingAndRatio().getMeeting().getDate()));
        String ratio = String.format(Locale.getDefault(), "%d / %d",
                success.getMeetingAndRatio().getRatio().getCurrent(),
                success.getMeetingAndRatio().getRatio().getMax());
        tvRatio.setText(ratio);
        viewModel.getAddress(success.getMeetingAndRatio().getMeeting().getLongitude(),
                success.getMeetingAndRatio().getMeeting().getLatitude())
                .observe(this, tvAddress::setText);
        tvDate.setOnClickListener(view -> handleExportEvent(success.getMeetingAndRatio()));
        tvAddress.setOnClickListener(view -> openMaps(success.getMeetingAndRatio()));
    }

    private void openMaps(MeetingAndRatio meetingAndRatio) {
        String uri = "http://maps.google.com/maps?q=loc:"
                + meetingAndRatio.getMeeting().getLatitude() + ","
                + meetingAndRatio.getMeeting().getLongitude();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));
    }

    private void handleExportEvent(MeetingAndRatio meetingAndRatio) {
        viewModel.exportEvent(meetingAndRatio, getContext());
    }

    private void handleProgress() {
        lavProgress.setVisibility(View.VISIBLE);
    }

    @NonNull
    private String getMeetingId() {
        Bundle arguments = getArguments();
        String meetingId = "-1";
        if (arguments != null) {
            meetingId = arguments.getString(MEETING_ID_KEY, "-1");
        }
        return meetingId;
    }

    public static void show(String meetingId, @Nullable FragmentManager fragmentManager) {
        Bundle arguments = new Bundle();
        arguments.putString(MEETING_ID_KEY, meetingId);
        Fragments.Dialog.show(TAG, fragmentManager, true, arguments, MeetingDetailsDialogFragment::new);
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        Fragments.Dialog.hide(TAG, fragmentManager, MeetingDetailsDialogFragment.class);
    }
}
