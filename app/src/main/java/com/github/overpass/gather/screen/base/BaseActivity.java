package com.github.overpass.gather.screen.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import butterknife.ButterKnife;

/**
 * @deprecated use {@link BaseActivityKt} instead
 */
@Deprecated
public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity {

    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        onInject();
        ButterKnife.bind(this);
        setupToolbar();
        viewModel = createViewModel();
        onBind();
    }

    protected void onInject() {
    }

    protected abstract int getLayoutRes();

    protected abstract VM createViewModel();

    protected void onActionBar(@NonNull ActionBar actionBar) {
    }

    protected void onBind() {
    }

    protected void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            onActionBar(actionBar);
        }
    }

    public static <A extends BaseActivity> void start(Context context,
                                                      Class<A> activityClass,
                                                      String key,
                                                      String value) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }
}
