package com.github.overpass.gather.ui.meeting.chat.attachments

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.overpass.gather.commons.abstractions.Result
import com.github.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import com.github.overpass.gather.commons.image.ImageConverter
import com.github.overpass.gather.model.usecase.attachment.PhotosUseCase
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase
import com.github.overpass.gather.ui.base.personal.DataViewModel
import com.github.overpass.gather.ui.map.Meeting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
        application: Application,
        imageSourceUseCase: ImageSourceUseCase,
        private val attachmentsUseCase: PhotosUseCase,
        private val photoUploadSuccessData: SingleLiveEvent<Void>,
        private val photoUploadProgressData: SingleLiveEvent<Int>,
        private val photoUploadErrorData: SingleLiveEvent<String>,
        private val suggestToChooseData: SingleLiveEvent<Boolean>,
        private val imageConverter: ImageConverter,
        private val meetingId: String
) : DataViewModel(application) {

    init {
        setImageSourceUseCase(imageSourceUseCase)
    }

    fun photoUploadSuccess(): LiveData<Void> = photoUploadSuccessData

    fun photoUploadProgress(): LiveData<Int> = photoUploadProgressData

    fun photoUploadError(): LiveData<String> = photoUploadErrorData

    fun getSuggestToChooseData(): LiveData<Boolean> = suggestToChooseData

    fun getMeeting(): LiveData<Meeting> = attachmentsUseCase.getMeeting(meetingId)

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun doAction() {
        val imageUri = selectedUri
        if (imageUri != null) {
            sendImage2(imageUri)
        } else {
            chooseImage()
        }
    }

    private fun chooseImage() {
        suggestToChooseData.value = true
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun sendImage2(imageUri: Uri) = viewModelScope.launch {
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
