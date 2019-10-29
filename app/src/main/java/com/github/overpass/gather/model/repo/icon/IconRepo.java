package com.github.overpass.gather.model.repo.icon;

import android.content.Context;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.create.MeetingType;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;

import javax.inject.Inject;

public class IconRepo {

    private final Context context;

    @Inject
    public IconRepo(Context context) {
        this.context = context.getApplicationContext();
    }

    public Icon getByType(int type) {
        int iconDrawable;
        if (type == MeetingType.BUSINESS.getType()) {
            iconDrawable = R.drawable.ic_case;
        } else if (type == MeetingType.ENTERTAINMENT.getType()) {
            iconDrawable = R.drawable.ic_beer;
        } else {
            iconDrawable = R.drawable.ic_bund;
        }
        return IconFactory.getInstance(context).fromResource(iconDrawable);
    }
}
