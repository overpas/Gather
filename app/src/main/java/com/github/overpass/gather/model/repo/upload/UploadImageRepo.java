package com.github.overpass.gather.model.repo.upload;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.data.FailedTask;
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus;
import com.github.overpass.gather.model.commons.Runners;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class UploadImageRepo {

    public static final String BUCKET_AVATARS = "Avatars";
    public static final String BUCKET_MEETINGS = "Meetings";

    private final FirebaseStorage storage;

    public UploadImageRepo(FirebaseStorage storage) {
        this.storage = storage;
    }

    public LiveData<ImageUploadStatus> saveImage(ContentResolver contentResolver,
                                                 Uri imageUri,
                                                 String bucket,
                                                 String userId,
                                                 String imageName) {
        MutableLiveData<ImageUploadStatus> data = new MutableLiveData<>();
        data.setValue(new ImageUploadStatus.Progress(0));
        Runners.io().execute(() -> {
            String path = bucket + "/" + userId + "/" + imageName + ".jpg";
            StorageReference storageReference = storage.getReference().child(path);
            storageReference.putBytes(uriToBytes(contentResolver, imageUri))
                    .addOnFailureListener(e -> data.postValue(new ImageUploadStatus.Error(e)))
                    .addOnProgressListener(temp -> {
                        long percent = temp.getBytesTransferred() / temp.getTotalByteCount();
                        data.postValue(new ImageUploadStatus.Progress(percent));
                    })
                    .onSuccessTask(Runners.io(), docRef -> storageReference.getDownloadUrl())
                    .addOnSuccessListener(uri -> {
                        data.postValue(new ImageUploadStatus.Success(uri));
                    })
                    .addOnFailureListener(e -> {
                        data.postValue(new ImageUploadStatus.Error(e));
                    });
        });
        return data;
    }

    @WorkerThread
    public byte[] uriToBytes(ContentResolver contentResolver, Uri imageUri) {
        Bitmap bitmap = uriToBitmap(contentResolver, imageUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @WorkerThread
    public Bitmap uriToBitmap(ContentResolver contentResolver, Uri imageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = contentResolver.query(imageUri, filePathColumn, null,
                null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        // Set the Image in ImageView after decoding the String
        return BitmapFactory.decodeFile(imgDecodableString);
    }
}
