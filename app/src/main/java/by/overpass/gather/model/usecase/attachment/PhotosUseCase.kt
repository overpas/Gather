package by.overpass.gather.model.usecase.attachment

import androidx.lifecycle.LiveData
import by.overpass.gather.commons.abstractions.Result
import by.overpass.gather.commons.abstractions.SimpleResult
import by.overpass.gather.data.repo.meeting.MeetingRepo
import by.overpass.gather.data.repo.meeting.MeetingRepo2
import by.overpass.gather.data.repo.upload.UploadImageRepo
import by.overpass.gather.ui.map.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class PhotosUseCase @Inject constructor(
        private val meetingRepo: MeetingRepo,
        private val meetingRepo2: MeetingRepo2,
        private val uploadImageRepo: UploadImageRepo
) {

    fun getMeeting(meetingId: String): LiveData<Meeting> {
        return meetingRepo.getLiveMeeting(meetingId)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend fun uploadPhoto(
            imageUri: ByteArray,
            meetingId: String
    ): Flow<Result<Unit>> = withContext(Dispatchers.IO) {
        val name = UUID.randomUUID().toString()
        uploadImageRepo.saveImage2(imageUri, UploadImageRepo.BUCKET_MEETINGS, meetingId, name)
                .map { result ->
                    when (result) {
                        is Result.Success ->
                            result.data
                                    .let { meetingRepo2.addPhotoUrl(meetingId, it) }
                                    .takeIf { it is SimpleResult.Error }
                                    ?.let { it as SimpleResult.Error }
                                    ?.run { Result.Error(exception) }
                                    ?: Result.Success(Unit)
                        is Result.Error -> Result.Error(result.exception)
                        is Result.Loading -> Result.Loading(result.percent)
                    }
                }
    }
}
