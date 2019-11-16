package by.overpass.gather.ui.meeting.chat.attachments

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import by.overpass.gather.commons.abstractions.Result
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.commons.image.ChooseImageHelper
import by.overpass.gather.commons.image.ImageConverter
import by.overpass.gather.model.usecase.attachment.PhotosUseCase
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import by.overpass.gather.model.usecase.userdata.PersonalDataUseCase
import by.overpass.gather.ui.base.personal.DataViewModel
import by.overpass.gather.ui.map.Meeting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
        application: Application,
        imageSourceUseCase: ImageSourceUseCase,
        chooseImageHelper: ChooseImageHelper,
        personalDataUseCase: PersonalDataUseCase,
        chosenImageData: MutableLiveData<Uri>,
        writePermissionDeniedData: SingleLiveEvent<Boolean>,
        readPermissionDeniedData: SingleLiveEvent<Boolean>,
        private val attachmentsUseCase: PhotosUseCase,
        private val photoUploadSuccessData: SingleLiveEvent<Void>,
        private val photoUploadProgressData: SingleLiveEvent<Int>,
        private val photoUploadErrorData: SingleLiveEvent<String>,
        private val suggestToChooseData: SingleLiveEvent<Boolean>,
        private val imageConverter: ImageConverter,
        private val meetingId: String
) : DataViewModel(
        application,
        chooseImageHelper,
        personalDataUseCase,
        chosenImageData,
        writePermissionDeniedData,
        readPermissionDeniedData,
        imageSourceUseCase
) {

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
