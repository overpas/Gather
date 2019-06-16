package com.github.overpass.gather.screen.meeting.chat.attachments.closeup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseActivity;

import butterknife.BindView;

public class CloseupActivity extends BaseActivity<CloseUpViewModel> {

    private static final String PHOTO_URL_KEY = "PHOTO_URL_KEY";

    @BindView(R.id.pvPhoto)
    PhotoView pvPhoto;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_closeup;
    }

    @Override
    protected CloseUpViewModel createViewModel() {
        return ViewModelProviders.of(this).get(CloseUpViewModel.class);
    }

    @Override
    protected void onActionBar(@NonNull ActionBar actionBar) {
        super.onActionBar(actionBar);
    }

    @Override
    protected void onBind() {
        super.onBind();
        Glide.with(this)
                .load(getPhotoUrl())
                .into(pvPhoto);
    }

    private String getPhotoUrl() {
        String photoUrl = "nothingness";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            photoUrl = extras.getString(PHOTO_URL_KEY);
        }
        return photoUrl;
    }

    public static void start(Context context, String photoUrl) {
        start(context, CloseupActivity.class, PHOTO_URL_KEY, photoUrl);
    }
}
