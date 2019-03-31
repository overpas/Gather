package com.github.overpass.gather.map.detail;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import androidx.annotation.NonNull;

public abstract class MapVerticalFlingListener implements MapboxMap.OnMoveListener {

    private float initX, initY, endX, endY;

    @Override
    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
        initX = detector.getCurrentEvent().getX();
        initY = detector.getCurrentEvent().getY();
    }

    @Override
    public void onMove(@NonNull MoveGestureDetector detector) {
    }

    @Override
    public void onMoveEnd(@NonNull MoveGestureDetector detector) {
        endX = detector.getCurrentEvent().getX();
        endY = detector.getCurrentEvent().getY();
        if (initY - endY > 200 && isHorizontalShiftInsignificant()) {
            onFlingDown();
        } else if (endY - initY > 200 && isHorizontalShiftInsignificant()) {
            onFlingUp();
        }
    }

    private boolean isHorizontalShiftInsignificant() {
        return !(Math.abs(initX - endX) > 400);
    }

    public abstract void onFlingUp();

    public abstract void onFlingDown();
}
