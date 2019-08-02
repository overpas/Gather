package com.github.overpass.gather.screen.meeting.chat.attachments

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.commons.image.ImageConverter
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.repo.meeting.MeetingRepo
import com.github.overpass.gather.model.repo.upload.UploadImageRepo
import com.github.overpass.gather.model.usecase.attachment.PhotosUseCase
import com.github.overpass.gather.screen.base.personal.DataViewModel
import com.github.overpass.gather.screen.map.Meeting
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PhotosViewModel(application: Application) : DataViewModel(application) {

    private val attachmentsUseCase: PhotosUseCase = PhotosUseCase(
            MeetingRepo(FirebaseFirestore.getInstance()),
            UploadImageRepo(FirebaseStorage.getInstance())
    )
    private val photoUploadSuccessData = SingleLiveEvent<Void>()
    private val photoUploadProgressData = SingleLiveEvent<Void>()
    private val photoUploadErrorData = SingleLiveEvent<String>()
    private val suggestToChooseData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val imageConverter: ImageConverter = ImageConverter(application.contentResolver)

    fun photoUploadSuccess(): LiveData<Void> = photoUploadSuccessData

    fun photoUploadProgress(): LiveData<Void> = photoUploadProgressData

    fun photoUploadError(): LiveData<String> = photoUploadErrorData

    fun getSuggestToChooseData(): LiveData<Boolean> = suggestToChooseData

    fun getMeeting(meetingId: String): LiveData<Meeting> = attachmentsUseCase.getMeeting(meetingId)

    fun doAction(meetingId: String) {
        val imageUri = selectedUri
        if (imageUri != null) {
            sendImage(imageUri, meetingId)
        } else {
            chooseImage()
        }
    }

    private fun chooseImage() {
        suggestToChooseData.value = true
    }

    private fun sendImage(imageUri: Uri, meetingId: String) {
        imageConverter.getBytes(imageUri)
                .onSuccessTask { attachmentsUseCase.loadPhoto(it!!, meetingId) }
                .toLiveData(
                        onStart = { PhotoUploadStatus.Progress },
                        onSuccessMap = { PhotoUploadStatus.Success },
                        onFailureMap = { PhotoUploadStatus.Error(it) }
                )
                .observeForever {
                    when (it) {
                        is PhotoUploadStatus.Success -> photoUploadSuccessData.call()
                        is PhotoUploadStatus.Progress -> photoUploadProgressData.call()
                        is PhotoUploadStatus.Error -> photoUploadErrorData.value = it.throwable.localizedMessage
                    }
                }

    }
}
