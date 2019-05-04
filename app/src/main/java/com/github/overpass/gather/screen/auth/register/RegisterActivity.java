package com.github.overpass.gather.screen.auth.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseActivity;
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity<RegisterViewModel>
        implements RegistrationController, PickImageDialogFragment.OnClickListener {

    private static final String INITIAL_STEP_KEY = "INITIAL_STEP_KEY";

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterViewModel createViewModel() {
        return ViewModelProviders.of(this, RegisterViewModelFactory.getInstance(getStep()))
                .get(RegisterViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setViewFlipperAnimations();
    }

    @Override
    protected void subscribe() {
        viewModel.getRegistrationProgressData().observe(this, this::moveToNextStep);
    }

    @Override
    public void moveToNextStep() {
        viewModel.next();
    }

    @Override
    public void onGallery() {
        viewModel.onGallery();
    }

    @Override
    public void onCamera() {
        viewModel.onCamera();
    }

    @Override
    public int getInitialStep() {
        return getStep();
    }

    private void moveToNextStep(Integer step) {
        if (viewModel.shouldShowNextStep(step)) {
            viewFlipper.setDisplayedChild(step);
        }
    }

    private void setViewFlipperAnimations() {
        Animation imgAnimationIn = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right);
        viewFlipper.setInAnimation(imgAnimationIn);
        Animation imgAnimationOut = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_left);
        viewFlipper.setOutAnimation(imgAnimationOut);
    }

    private int getStep() {
        int step = 0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            step = bundle.getInt(INITIAL_STEP_KEY, 0);
        }
        return step;
    }

    public static void start(Context context, int initialStep) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(INITIAL_STEP_KEY, initialStep);
        context.startActivity(intent);
    }
}
