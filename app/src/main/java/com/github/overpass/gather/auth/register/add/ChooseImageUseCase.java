package com.github.overpass.gather.auth.register.add;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.github.overpass.gather.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

@SuppressWarnings("SameParameterValue")
public class ChooseImageUseCase {

    private static final String TAG = "ChooseImageUseCase";

    void chooseFromGallery(Fragment fragment, int requestCode) {
        // Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // We pass an extra array with the accepted mime types.
        // This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        fragment.startActivityForResult(intent, requestCode);
    }

    @Nullable
    String chooseFromCamera(Fragment fragment, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = null;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            Log.e(TAG, "chooseFromCamera: " + e.getLocalizedMessage(), e);
        }
        if (imageFile == null) {
            return null;
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, createPhotoUri(fragment, imageFile));
            fragment.startActivityForResult(intent, requestCode);
            // Save a file: path for using again
            return "file://" + imageFile.getAbsolutePath();
        }
    }

    private Uri createPhotoUri(Fragment fragment, File imageFile) {
        Context context = fragment.getContext();
        String authority = BuildConfig.APPLICATION_ID + ".provider";
        return FileProvider.getUriForFile(context, authority, imageFile);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createImageFile() throws IOException {
        // Create an image file name
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = format.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // This is the directory in which the file will be created. This is the default location
        // of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        storageDir.mkdirs();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
}
