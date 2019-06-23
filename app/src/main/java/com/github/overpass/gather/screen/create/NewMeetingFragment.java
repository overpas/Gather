package com.github.overpass.gather.screen.create;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseFragment;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.github.overpass.gather.screen.map.SaveMeetingStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Date;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.dateOf;
import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

public class NewMeetingFragment extends BaseFragment<NewMeetingViewModel> {

    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";

    @BindView(R.id.tietName)
    TextInputEditText tietName;
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    @BindView(R.id.bnvMeetingType)
    BottomNavigationView bnvMeetingType;
    @BindView(R.id.npMaxPeople)
    NumberPicker npMaxPeople;
    @BindView(R.id.switchPrivate)
    SwitchMaterial switchPrivate;

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
        MeetingType meetingType;
        switch (bnvMeetingType.getSelectedItemId()) {
            case R.id.type_business:
                meetingType = MeetingType.BUSINESS;
                break;
            case R.id.type_entertainment:
                meetingType = MeetingType.ENTERTAINMENT;
                break;
            default:
                meetingType = MeetingType.PROTEST;
        }
        double latitude = getArguments().getDouble(KEY_LATITUDE);
        double longitude = getArguments().getDouble(KEY_LONGITUDE);
        String title = textOf(tietName);
        Date date = dateOf(datePicker);
        int maxPeople = npMaxPeople.getValue();
        boolean isPrivate = switchPrivate.isChecked();
        getViewModel().createMeeting(latitude, longitude, title, date, meetingType, maxPeople, isPrivate)
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
