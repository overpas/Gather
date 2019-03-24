package com.github.overpass.gather.auth.register.add;

import android.content.Intent;

import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class UploadImageService extends JobIntentService {


    public UploadImageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
    }
}
