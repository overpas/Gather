package com.github.overpass.gather.create;

import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseFragment;

import androidx.lifecycle.ViewModelProviders;

public class NewMeetingFragment extends BaseFragment<NewMeetingViewModel> {

    @Override
    protected NewMeetingViewModel createViewModel() {
        return ViewModelProviders.of(this).get(NewMeetingViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_new_meeting;
    }

    public static NewMeetingFragment newInstance() {
        return new NewMeetingFragment();
    }
}
