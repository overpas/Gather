package by.overpass.gather.data.repo.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import by.overpass.gather.commons.android.lifecycle.LiveDataUtils
import by.overpass.gather.data.repo.meeting.MeetingsMetadata
import by.overpass.gather.data.repo.user.UserData
import by.overpass.gather.ui.meeting.chat.DeleteStatus
import by.overpass.gather.ui.meeting.chat.MessageStatus
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import javax.inject.Inject

class MessageRepo @Inject constructor(
        private val firestore: FirebaseFirestore
) : MeetingsMetadata {

    fun messages(meetingId: String): LiveData<MessageStatus> {
        val messagesData = MutableLiveData<MessageStatus>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(MeetingsMetadata.Messages.COLLECTION)
                .orderBy(MeetingsMetadata.Messages.FIELD_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener(object : MessageMapper() {
                    override fun onSuccess(success: MessageStatus.Success) {
                        messagesData.value = success
                    }

                    override fun onError(error: MessageStatus.Error) {
                        messagesData.value = error
                    }
                })
        return messagesData
    }

    fun send(meetingId: String, input: String, userData: UserData) {
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(MeetingsMetadata.Messages.COLLECTION)
                .add(BaseMessage(
                        input,
                        Date(),
                        userData.id,
                        userData.name,
                        userData.photoUrl
                ))
    }

    fun delete(meetingId: String, ids: List<String>): LiveData<DeleteStatus> {
        val separateDeletions = ids
                .map { id ->
                    delete(meetingId, id)
                }
        val deletionsData = LiveDataUtils.zip(separateDeletions)
        return Transformations.map(deletionsData) { input ->
            var deleteStatus: DeleteStatus = DeleteStatus.Progress()
            var successCount = 0
            for (status in input) {
                if (status is DeleteStatus.Error || status is DeleteStatus.Progress) {
                    deleteStatus = status
                    break
                } else {
                    successCount++
                }
            }
            if (successCount == input.size) {
                deleteStatus = DeleteStatus.Success()
            }
            deleteStatus
        }
    }

    private fun delete(meetingId: String, messageId: String): LiveData<DeleteStatus> {
        val deleteData = MutableLiveData<DeleteStatus>()
        deleteData.setValue(DeleteStatus.Progress())
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(MeetingsMetadata.Messages.COLLECTION)
                .document(messageId)
                .delete()
                .addOnSuccessListener { s -> deleteData.setValue(DeleteStatus.Success()) }
                .addOnFailureListener { e -> deleteData.setValue(DeleteStatus.Error(e)) }
        return deleteData
    }

    private abstract class MessageMapper : EventListener<QuerySnapshot> {

        override fun onEvent(snapshots: QuerySnapshot?,
                             e: FirebaseFirestoreException?) {
            if (e != null) {
                onError(MessageStatus.Error(e))
            }
            if (snapshots != null) {
                val messages = snapshots.documents
                        .map { toMessage(it) }
                onSuccess(MessageStatus.Success(messages))
            }
        }

        internal abstract fun onSuccess(success: MessageStatus.Success)

        internal abstract fun onError(error: MessageStatus.Error)

        private fun toMessage(documentSnapshot: DocumentSnapshot): Message {
            return Message(
                    documentSnapshot.id,
                    documentSnapshot.getString(MeetingsMetadata.Messages.FIELD_TEXT),
                    documentSnapshot.getDate(MeetingsMetadata.Messages.FIELD_DATE),
                    documentSnapshot.getString(MeetingsMetadata.Messages.FIELD_AUTHOR_ID),
                    documentSnapshot.getString(MeetingsMetadata.Messages.FIELD_AUTHOR_NAME),
                    documentSnapshot.getString(MeetingsMetadata.Messages.FIELD_AUTHOR_PHOTO_URL)
            )
        }
    }
}
