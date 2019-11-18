package by.overpass.gather.data.repo.meeting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import by.overpass.gather.commons.concurrency.Runners
import by.overpass.gather.ui.create.MeetingType
import by.overpass.gather.ui.map.AuthUser
import by.overpass.gather.ui.map.Meeting
import by.overpass.gather.ui.map.SaveMeetingStatus
import by.overpass.gather.ui.map.detail.Current2MaxPeopleRatio
import by.overpass.gather.ui.meeting.MeetingAndRatio
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import javax.inject.Inject

class MeetingRepo @Inject constructor(
        private val firestore: FirebaseFirestore
) : MeetingsMetadata {

    private val meetingStatus = MediatorLiveData<LoadMeetingStatus>()

    fun getMeetings(latitude: Double,
                    longitude: Double,
                    radius: Double): LiveData<Map<String, Meeting>> {
        val meetingData = MutableLiveData<Map<String, Meeting>>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .whereGreaterThanOrEqualTo(MeetingsMetadata.FIELD_LATITUDE, latitude - radius)
                .whereLessThanOrEqualTo(MeetingsMetadata.FIELD_LATITUDE, latitude + radius)
                //              .whereGreaterThanOrEqualTo(FIELD_LONGITUDE, longitude - radius)
                //              .whereLessThanOrEqualTo(FIELD_LONGITUDE, longitude + radius)
                //              FUCK YOU FIREBASE
                .get()
                .addOnSuccessListener { docs ->
                    val map = docs.documents
                            .associateBy(
                                    { it.id },
                                    { it.toObject(Meeting::class.java)!! }
                            )
                    meetingData.setValue(map)
                }
                .addOnFailureListener { e -> Log.e(TAG, e.localizedMessage, e) }
        return meetingData
    }

    fun save(latitude: Double,
             longitude: Double,
             date: Date,
             name: String,
             type: MeetingType,
             maxPeople: Int,
             isPrivate: Boolean,
             authUser: AuthUser?): LiveData<SaveMeetingStatus> {
        val resultData = MutableLiveData<SaveMeetingStatus>()
        resultData.value = SaveMeetingStatus.Progress()
        val meetingId = arrayOfNulls<String>(1)
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .add(
                        Meeting(
                                name,
                                latitude,
                                longitude,
                                date,
                                type.type,
                                maxPeople,
                                isPrivate
                        )
                )
                .addOnFailureListener { e -> resultData.setValue(SaveMeetingStatus.Error(e)) }
                .onSuccessTask<DocumentReference>(Runners.io(), SuccessContinuation onSuccessTask@{ docRef ->
                    if (authUser != null) {
                        meetingId[0] = docRef!!.id
                        return@onSuccessTask firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                                .add(
                                        Meeting(
                                                name,
                                                latitude,
                                                longitude,
                                                date,
                                                type.type,
                                                maxPeople,
                                                isPrivate
                                        )
                                )
                                .addOnFailureListener {
                                    resultData.setValue(SaveMeetingStatus.Error(it))
                                }
                                .onSuccessTask<DocumentReference> {
                                    docRef.collection(MeetingsMetadata.Users.COLLECTION)
                                            .add(authUser)
                                }
                    }
                    Tasks.forException<DocumentReference>(FirebaseException("Something Went Wrong!"))
                })
                .addOnSuccessListener { result -> resultData.setValue(SaveMeetingStatus.Success(meetingId[0])) }
                .addOnFailureListener { e -> resultData.setValue(SaveMeetingStatus.Error(e)) }
        return resultData
    }

    fun getCurrent2MaxRatio(id: String): LiveData<Current2MaxPeopleRatio> {
        val maxPeopleData = MutableLiveData<Current2MaxPeopleRatio>()
        val numbers = IntArray(2)
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(id)
                .get()
                .onSuccessTask<QuerySnapshot>(Runners.io(), SuccessContinuation { docRef ->
                    numbers[0] = docRef!!.getLong("maxPeople")!!.toLong().toInt()
                    docRef.reference
                            .collection(MeetingsMetadata.Users.COLLECTION)
                            .get()
                })
                .addOnSuccessListener { queryDocumentSnapshots ->
                    numbers[1] = queryDocumentSnapshots.size()
                    maxPeopleData.setValue(Current2MaxPeopleRatio(numbers[1], numbers[0]))
                }
                .addOnFailureListener { e -> maxPeopleData.setValue(Current2MaxPeopleRatio.FAILED) }
        return maxPeopleData
    }

    fun getFullMeeting(meetingId: String): LiveData<LoadMeetingStatus> {
        if (meetingStatus.value != null && meetingStatus.value is LoadMeetingStatus.Success) {
            return meetingStatus
        }
        meetingStatus.value = LoadMeetingStatus.Progress()
        val meetingAndRatioData = Transformations.switchMap(
                getMeeting(meetingId)
        ) { meeting ->
            Transformations.switchMap(getCurrent2MaxRatio(meetingId)) { ratio ->
                val resultData = MutableLiveData<MeetingAndRatio>()
                resultData.value = MeetingAndRatio(meeting, ratio)
                resultData
            }
        }
        meetingStatus.addSource(meetingAndRatioData) { meetingAndRatio ->
            if (meetingAndRatio == null || meetingAndRatio.meeting == Meeting.EMPTY
                    || meetingAndRatio.ratio == Current2MaxPeopleRatio.FAILED) {
                meetingStatus.setValue(LoadMeetingStatus.Error())
            } else {
                meetingStatus.setValue(LoadMeetingStatus.Success(meetingAndRatio))
            }
        }
        return meetingStatus
    }

    fun getMeeting(meetingId: String): LiveData<Meeting> {
        val meetingStatus = MutableLiveData<Meeting>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .get()
                .addOnSuccessListener { doc -> meetingStatus.setValue(doc.toObject(Meeting::class.java)) }
                .addOnFailureListener { e -> meetingStatus.setValue(Meeting.EMPTY) }
        return meetingStatus
    }

    fun getLiveMeeting(meetingId: String): LiveData<Meeting> {
        val meetingStatus = MutableLiveData<Meeting>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .addSnapshotListener { doc, e ->
                    if (doc != null && e == null) {
                        val meeting = doc.toObject(Meeting::class.java)
                        meetingStatus.value = meeting
                    }
                }
        return meetingStatus
    }

    fun isUserAllowed(authUser: AuthUser?, meetingId: String): LiveData<Boolean> {
        return isUserListed(MeetingsMetadata.Users.COLLECTION, authUser, meetingId)
    }

    fun isAlreadyEnrolled(authUser: AuthUser?, meetingId: String): LiveData<Boolean> {
        return isUserListed(MeetingsMetadata.PendingUsers.COLLECTION, authUser, meetingId)
    }

    private fun isUserListed(subcollection: String,
                             authUser: AuthUser?,
                             meetingId: String): LiveData<Boolean> {
        val isAllowedData = MutableLiveData<Boolean>()
        if (authUser == null) {
            isAllowedData.value = false
            return isAllowedData
        }
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(subcollection)
                .whereEqualTo(MeetingsMetadata.Users.FIELD_ID, authUser.id)
                .get()
                .addOnSuccessListener { doc -> isAllowedData.setValue(!doc.isEmpty) }
                .addOnFailureListener { e -> isAllowedData.setValue(false) }
        return isAllowedData
    }

    fun searchByName(text: String): LiveData<List<MeetingWithId>> {
        val meetingData = MutableLiveData<List<MeetingWithId>>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .whereEqualTo(MeetingsMetadata.FIELD_NAME, text)
                .addSnapshotListener { docs, e ->
                    if (e != null || docs == null || docs.isEmpty) {
                        meetingData.setValue(null)
                    } else {
                        val meetings = docs.documents
                                .map { doc ->
                                    MeetingWithId(
                                            doc.toObject(Meeting::class.java),
                                            doc.id
                                    )
                                }
                        meetingData.setValue(meetings)
                    }
                }
        return meetingData
    }

    companion object {

        private const val TAG = "MeetingRepo"
    }
}
