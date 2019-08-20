package com.github.overpass.gather.screen.meeting.chat.attachments

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.overpass.gather.model.commons.Result
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.commons.image.ImageConverter
import com.github.overpass.gather.model.repo.meeting.MeetingRepo
import com.github.overpass.gather.model.repo.meeting.MeetingRepo2
import com.github.overpass.gather.model.repo.upload.UploadImageRepo
import com.github.overpass.gather.model.usecase.attachment.PhotosUseCase
import com.github.overpass.gather.screen.base.personal.DataViewModel
import com.github.overpass.gather.screen.map.Meeting
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PhotosViewModel(application: Application) : DataViewModel(application) {

    private val attachmentsUseCase: PhotosUseCase = PhotosUseCase(
            MeetingRepo(FirebaseFirestore.getInstance()),
            MeetingRepo2(FirebaseFirestore.getInstance()),
            UploadImageRepo(
                    FirebaseStorage.getInstance(),
                    ImageConverter(application.contentResolver)
            )
    )
    private val photoUploadSuccessData = SingleLiveEvent<Void>()
    private val photoUploadProgressData = SingleLiveEvent<Int>()
    private val photoUploadErrorData = SingleLiveEvent<String>()
    private val suggestToChooseData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val imageConverter: ImageConverter = ImageConverter(application.contentResolver)

    fun photoUploadSuccess(): LiveData<Void> = photoUploadSuccessData

    fun photoUploadProgress(): LiveData<Int> = photoUploadProgressData

    fun photoUploadError(): LiveData<String> = photoUploadErrorData

    fun getSuggestToChooseData(): LiveData<Boolean> = suggestToChooseData

    fun getMeeting(meetingId: String): LiveData<Meeting> = attachmentsUseCase.getMeeting(meetingId)

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun doAction(meetingId: String) {
        val imageUri = selectedUri
        if (imageUri != null) {
            sendImage2(imageUri, meetingId)
        } else {
            chooseImage()
        }
    }

    private fun chooseImage() {
        suggestToChooseData.value = true
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun sendImage2(imageUri: Uri, meetingId: String) = viewModelScope.launch {
        val imageBytes = imageConverter.getImageBytes(imageUri)
        attachmentsUseCase.uploadPhoto(imageBytes, meetingId)
//                // Omit multiple Result.Loading values
//                .filterNot { it is Result.Loading }
//                .onStart { emit(Result.Loading()) }
                .collect {
                    when (it) {
                        is Result.Success -> photoUploadSuccessData.call()
                        is Result.Loading -> photoUploadProgressData.value = it.percent
                        is Result.Error -> photoUploadErrorData.value = it.exception.localizedMessage
                    }
                }
    }
}
