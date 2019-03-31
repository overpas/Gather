package com.github.overpass.gather.create;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseFragment;
import com.github.overpass.gather.dialog.ProgressDialogFragment;
import com.github.overpass.gather.map.SaveMeetingStatus;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

import static com.github.overpass.gather.UIUtil.dateOf;
import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;

public class NewMeetingFragment extends BaseFragment<NewMeetingViewModel> {

    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";

    @BindView(R.id.tietName)
    TextInputEditText tietName;
    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_meeting, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("New Meeting");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                create();
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void create() {
        double latitude = getArguments().getDouble(KEY_LATITUDE);
        double longitude = getArguments().getDouble(KEY_LONGITUDE);
        String title = textOf(tietName);
        Date date = dateOf(datePicker);
        viewModel.createMeeting(latitude, longitude, title, date)
                .observe(getViewLifecycleOwner(), this::handleSaveMeetingStatus);
    }

    private void handleSaveMeetingStatus(SaveMeetingStatus saveMeetingStatus) {
        switch (saveMeetingStatus.tag()) {
            case SaveMeetingStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                String message = saveMeetingStatus.as(SaveMeetingStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(tietName, message);
                break;
            case SaveMeetingStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietName, "Success");
                getActivity().finish();
                break;
            case SaveMeetingStatus.PROGRESS:
                ProgressDialogFragment.show(getFragmentManager());
                break;
            case SaveMeetingStatus.EMPTY_NAME:
                ProgressDialogFragment.hide(getFragmentManager());
                tietName.setError("Please, type a proper name");
                break;
        }
    }

    @Override
    protected NewMeetingViewModel createViewModel() {
        return ViewModelProviders.of(this).get(NewMeetingViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_new_meeting;
    }

    public static NewMeetingFragment newInstance(double latitude, double longitude) {
        Bundle args = new Bundle();
        args.putDouble(KEY_LATITUDE, latitude);
        args.putDouble(KEY_LONGITUDE, longitude);
        NewMeetingFragment fragment = new NewMeetingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}