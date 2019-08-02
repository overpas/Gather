package com.github.overpass.gather.model.usecase.attachment

import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.exception.PhotoUploadException
import com.github.overpass.gather.model.commons.flatMap
import com.github.overpass.gather.model.commons.mapToSuccess

import com.github.overpass.gather.model.repo.meeting.MeetingRepo
import com.github.overpass.gather.model.repo.upload.UploadImageRepo
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus
import com.github.overpass.gather.screen.map.Meeting
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotoUploadStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

import java.util.UUID

class PhotosUseCase(
        private val meetingRepo: MeetingRepo,
        private val uploadImageRepo: UploadImageRepo
) {

    fun getMeeting(meetingId: String): LiveData<Meeting> {
        return meetingRepo.getLiveMeeting(meetingId)
    }

    fun loadPhoto(imageUri: ByteArray,
                  meetingId: String
    ): Task<PhotoUploadStatus> {
        val name = UUID.randomUUID().toString()
        return uploadImageRepo.saveImage(imageUri, UploadImageRepo.BUCKET_MEETINGS, meetingId, name)
                .flatMap {
                    return@flatMap when (it) {
                        is ImageUploadStatus.Success -> meetingRepo.addPhoto(meetingId, it.uri.toString())
                                .mapToSuccess(
                                        successMapper = { PhotoUploadStatus.Success },
                                        failureMapper = { PhotoUploadStatus.Error(PhotoUploadException()) }
                                )
                        is ImageUploadStatus.Error -> Tasks.forResult<PhotoUploadStatus>(PhotoUploadStatus.Error(it.throwable))
                        else -> Tasks.forResult<PhotoUploadStatus>(PhotoUploadStatus.Progress)
                    }
                }
    }
}
