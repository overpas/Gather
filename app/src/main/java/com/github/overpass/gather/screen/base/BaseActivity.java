package com.github.overpass.gather.screen.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import butterknife.ButterKnife;

public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity {

    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        setupToolbar();
        viewModel = createViewModel();
        subscribe();
    }

    protected abstract int getLayoutRes();

    protected abstract VM createViewModel();

    protected void onActionBar(@NonNull ActionBar actionBar) {
    }

    protected void subscribe() {
    }

    protected void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            onActionBar(getSupportActionBar());
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
