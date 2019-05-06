package com.github.overpass.gather.screen.create;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.R;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class NewMeetingActivity extends AppCompatActivity {

    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";

    public static void start(LatLng coordinates, Context context) {
        double latitude = coordinates.getLatitude();
        double longitude = coordinates.getLongitude();
        Intent intent = new Intent(context, NewMeetingActivity.class);
        intent.putExtra(KEY_LATITUDE, latitude);
        intent.putExtra(KEY_LONGITUDE, longitude);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        setupToolbar();
        if (savedInstanceState == null) {
            double latitude = getIntent().getExtras().getDouble(KEY_LATITUDE);
            double longitude = getIntent().getExtras().getDouble(KEY_LONGITUDE);
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flNewMeetingContainer,
                    NewMeetingFragment.newInstance(latitude, longitude), false);
        }
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.new_meeting));
            actionBar.setDisplayHomeAsUpEnabled(true);
            int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
            actionBar.setBackgroundDrawable(new ColorDrawable(primaryColor));
        }
    }
}
