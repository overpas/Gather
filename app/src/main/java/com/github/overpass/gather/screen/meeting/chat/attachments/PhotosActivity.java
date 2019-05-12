package com.github.overpass.gather.screen.meeting.chat.attachments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.screen.base.BaseActivity;
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment;

import static com.github.overpass.gather.model.commons.Constants.MEETING_ID_KEY;

public class PhotosActivity extends BaseActivity<GeneralPhotoViewModel>
        implements PickImageDialogFragment.OnClickListener {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photos;
    }

    @Override
    protected GeneralPhotoViewModel createViewModel() {
        return ViewModelProviders.of(this).get(GeneralPhotoViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flPhotosContainer,
                    PhotosFragment.newInstance(getMeetingId()), false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActionBar(@NonNull ActionBar actionBar) {
        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(primaryColor));
        actionBar.setTitle(R.string.attachments);
    }

    @Override
    public void onGallery() {
        viewModel.onGallery();
    }

    @Override
    public void onCamera() {
        viewModel.onCamera();
    }

    private String getMeetingId() {
        String meetingId = "-1";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            meetingId = extras.getString(MEETING_ID_KEY);
        }
        return meetingId;
    }

    public static void start(Context context, String meetingId) {
        start(context, PhotosActivity.class, MEETING_ID_KEY, meetingId);
    }
}
