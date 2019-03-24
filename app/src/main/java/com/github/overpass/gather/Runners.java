package com.github.overpass.gather;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Runners {

    private static class UIExecutorHolder {
        private static final Executor executor = new Executor() {

            private Handler uiHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable command) {
                uiHandler.post(command);
            }
        };
    }

    private static class IOExecutorHolder {
        private static final Executor executor = Executors.newFixedThreadPool(2);
    }

    public static Executor ui() {
        return UIExecutorHolder.executor;
    }

    public static Executor io() {
        return IOExecutorHolder.executor;
    }

    public static Executor intensive() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }
}
