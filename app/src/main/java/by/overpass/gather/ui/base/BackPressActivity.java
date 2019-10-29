package by.overpass.gather.ui.base;

import androidx.lifecycle.ViewModel;

import com.annimon.stream.Stream;

public abstract class BackPressActivity<VM extends ViewModel> extends BaseActivityKt<VM> {

    @Override
    public void onBackPressed() {
        if (!Stream.of(getSupportFragmentManager().getFragments())
                .filter(fragment -> fragment instanceof BackPressFragment)
                .map(fragment -> (BackPressFragment) fragment)
                .anyMatch(BackPressFragment::handleBackPress)) {
            super.onBackPressed();
        }
    }
}
