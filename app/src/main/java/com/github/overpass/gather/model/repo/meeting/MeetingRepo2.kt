package com.github.overpass.gather.model.repo.meeting

import com.github.overpass.gather.model.commons.SimpleResult
import com.github.overpass.gather.model.commons.await
import com.github.overpass.gather.model.commons.exception.DefaultException
import com.github.overpass.gather.model.repo.meeting.MeetingsMetadata.COLLECTION_MEETINGS
import com.github.overpass.gather.model.repo.meeting.MeetingsMetadata.FIELD_PHOTOS
import com.github.overpass.gather.screen.map.Meeting
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MeetingRepo2 @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun addPhotoUrl(
            meetingId: String,
            photoUrl: String
    ): SimpleResult = withContext(Dispatchers.IO) {
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .get()
                .onSuccessTask { doc ->
                    doc?.run { toObject(Meeting::class.java) }
                            ?.photos
                            ?.also { it.add(photoUrl) }
                            ?.let {
                                firestore.collection(COLLECTION_MEETINGS)
                                        .document(meetingId)
                                        .update(FIELD_PHOTOS, it)
                            }
                            ?: Tasks.forException(DefaultException())
                }
                .await()
                .takeIf { it.isSuccessful }
                ?.run { SimpleResult.Success }
                ?: SimpleResult.Error()
    }
}