package com.github.overpass.gather.model.repo.upload

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.model.commons.Result
import com.github.overpass.gather.model.commons.Runners
import com.github.overpass.gather.model.commons.image.ImageConverter
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus
import com.google.android.gms.tasks.SuccessContinuation
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UploadImageRepo(
        private val storage: FirebaseStorage,
        private val imageConverter: ImageConverter
) {

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
            val imageBytes = imageConverter.resolveBytesFromImageUri(imageUri)
            storageReference.putBytes(imageBytes)
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

    @ExperimentalCoroutinesApi
    fun saveImage2(
            imageBytes: ByteArray,
            bucket: String,
            folder: String,
            imageName: String
    ): Flow<Result<String>> = callbackFlow {
        send(Result.Loading())
        val path = "$bucket/$folder/$imageName$IMAGE_EXTENSION"
        val storageReference = storage.reference.child(path)
        storageReference.putBytes(imageBytes)
                .addOnProgressListener { temp ->
                    val current = temp.bytesTransferred.toDouble()
                    val total = temp.totalByteCount.toDouble()
                    val percent = (current / total * 100).toInt()
                    offer(Result.Loading(percent))
                }
                .onSuccessTask { storageReference.downloadUrl }
                .addOnSuccessListener { offer(Result.Success(it.toString())) }
                .addOnFailureListener { offer(Result.Error(it)) }
        awaitClose()
    }

    companion object {
        const val IMAGE_EXTENSION = ".jpg"
        const val BUCKET_AVATARS = "Avatars"
        const val BUCKET_MEETINGS = "Meetings"
    }
}
