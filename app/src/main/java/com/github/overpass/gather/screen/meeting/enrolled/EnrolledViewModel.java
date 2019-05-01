package com.github.overpass.gather.screen.meeting.enrolled;


import androidx.lifecycle.ViewModel;

public class EnrolledViewModel extends ViewModel {

    private boolean shouldPlayAnim = true;

    public boolean shouldAnimate() {
        if (shouldPlayAnim) {
            shouldPlayAnim = false;
            return true;
        } else {
            return false;
        }
    }
}
