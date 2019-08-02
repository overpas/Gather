package com.github.overpass.gather.model.repo.upload

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.model.commons.Runners
import com.github.overpass.gather.model.commons.image.uriToBytes
import com.github.overpass.gather.model.commons.mapToSuccess
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class UploadImageRepo(private val storage: FirebaseStorage) {

    @Deprecated("""Use this:
        fun saveImage(imageBytes: ByteArray,
                  bucket: String,
                  folder: String,
                  imageName: String
        ): Task<ImageUploadStatus>
    """)
    fun saveImage(contentResolver: ContentResolver,
                  imageUri: Uri,
                  bucket: String,
                  folder: String,
                  imageName: String): LiveData<ImageUploadStatus> {
        val data = MutableLiveData<ImageUploadStatus>()
        data.setValue(ImageUploadStatus.Progress(0))
        Runners.io().execute {
            val path = "$bucket/$folder/$imageName$IMAGE_EXTENSION"
            val storageReference = storage.reference.child(path)
            storageReference.putBytes(uriToBytes(contentResolver, imageUri))
                    .addOnFailureListener { e -> data.postValue(ImageUploadStatus.Error(e)) }
                    .addOnProgressListener { temp ->
                        val percent = temp.bytesTransferred / temp.totalByteCount
                        data.postValue(ImageUploadStatus.Progress(percent))
                    }
                    .onSuccessTask<Uri>(Runners.io(), SuccessContinuation { docRef ->
                        storageReference.downloadUrl
                    })
                    .addOnSuccessListener { uri -> data.postValue(ImageUploadStatus.Success(uri)) }
                    .addOnFailureListener { e -> data.postValue(ImageUploadStatus.Error(e)) }
        }
        return data
    }

    fun saveImage(imageBytes: ByteArray,
                  bucket: String,
                  folder: String,
                  imageName: String
    ): Task<ImageUploadStatus> {
        val path = "$bucket/$folder/$imageName$IMAGE_EXTENSION"
        val storageReference = storage.reference.child(path)
        return storageReference.putBytes(imageBytes)
                .onSuccessTask<Uri>(Runners.io(), SuccessContinuation { docRef ->
                    storageReference.downloadUrl
                })
                .mapToSuccess(
                        successMapper = { ImageUploadStatus.Success(it!!) },
                        failureMapper = { ImageUploadStatus.Error(it) }
                )
    }

    companion object {
        const val IMAGE_EXTENSION = ".jpg"
        const val BUCKET_AVATARS = "Avatars"
        const val BUCKET_MEETINGS = "Meetings"
    }
}
