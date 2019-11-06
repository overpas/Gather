package by.overpass.gather.data.repo.meeting

import by.overpass.gather.commons.abstractions.SimpleResult
import by.overpass.gather.commons.firebase.await
import by.overpass.gather.commons.exception.DefaultException
import by.overpass.gather.data.repo.meeting.MeetingsMetadata.COLLECTION_MEETINGS
import by.overpass.gather.data.repo.meeting.MeetingsMetadata.FIELD_PHOTOS
import by.overpass.gather.ui.map.Meeting
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