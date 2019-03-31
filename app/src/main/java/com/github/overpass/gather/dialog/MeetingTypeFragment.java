package com.github.overpass.gather.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.overpass.gather.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingTypeFragment extends BottomSheetDialogFragment {

    private static final String TAG = "MeetingTypeFragment";

    public static void show(FragmentManager supportFragmentManager) {
        MeetingTypeFragment fragment = new MeetingTypeFragment();
        fragment.show(supportFragmentManager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_type, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.ivBusiness)
    public void onBusinessClick() {

    }

    @OnClick(R.id.ivEntertainment)
    public void onEntertainmentClick() {

    }

    @OnClick(R.id.ivProtest)
    public void onProtestClick() {

    }
}
