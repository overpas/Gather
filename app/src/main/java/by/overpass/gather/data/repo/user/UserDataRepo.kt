package by.overpass.gather.data.repo.user

import androidx.arch.core.util.Function
import androidx.core.util.Consumer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import by.overpass.gather.commons.android.lifecycle.LiveDataUtils
import by.overpass.gather.commons.concurrency.Runners
import by.overpass.gather.data.repo.meeting.MeetingsMetadata
import by.overpass.gather.ui.map.AuthUser
import by.overpass.gather.ui.meeting.chat.users.model.Acceptance
import by.overpass.gather.ui.meeting.chat.users.model.LoadUsersStatus
import by.overpass.gather.ui.meeting.chat.users.model.UserModel
import com.google.android.gms.tasks.SuccessContinuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import javax.inject.Inject

class UserDataRepo @Inject constructor(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore
) : MeetingsMetadata, UsersData {

    fun getCurrentUserData(onSuccess: Consumer<FirebaseUser>, onError: Runnable) {
        val user = auth.currentUser
        if (user != null) {
            onSuccess.accept(user)
        } else {
            onError.run()
        }
    }

    fun getMembers(meetingId: String): LiveData<LoadUsersStatus> {
        return getUsers(
                meetingId,
                MeetingsMetadata.Users.COLLECTION,
                Function<List<UserModel>, LoadUsersStatus.MembersSuccess> {
                    LoadUsersStatus.MembersSuccess(it)
                }
        )
    }

    fun getPendingUsers(meetingId: String): LiveData<LoadUsersStatus> {
        return getUsers(meetingId, MeetingsMetadata.PendingUsers.COLLECTION,
                Function<List<UserModel>, LoadUsersStatus.PendingSuccess> { LoadUsersStatus.PendingSuccess(it) })
    }

    fun accept(meetingId: String, userId: String): LiveData<Acceptance> {
        val acceptanceData = MutableLiveData<Acceptance>()
        acceptanceData.setValue(Acceptance.PROGRESS)
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(MeetingsMetadata.PendingUsers.COLLECTION)
                .whereEqualTo(MeetingsMetadata.PendingUsers.FIELD_ID, userId)
                .get()
                .onSuccessTask<DocumentReference>(Runners.io(), SuccessContinuation { querySnapshot ->
                    val documentSnapshot = querySnapshot!!.documents[0]
                    val authUser = documentSnapshot.toObject(AuthUser::class.java)
                    firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                            .document(meetingId)
                            .collection(MeetingsMetadata.Users.COLLECTION)
                            .add(authUser!!)
                })
                .onSuccessTask<Void>(Runners.io(), SuccessContinuation { documentReference ->
                    firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                            .document(meetingId)
                            .collection(MeetingsMetadata.PendingUsers.COLLECTION)
                            .whereEqualTo(MeetingsMetadata.PendingUsers.FIELD_ID, userId)
                            .get()
                            .onSuccessTask<Void>(Runners.io(), SuccessContinuation { snapshot ->
                                val docId = snapshot!!.documents[0].id
                                firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                                        .document(meetingId)
                                        .collection(MeetingsMetadata.PendingUsers.COLLECTION)
                                        .document(docId)
                                        .delete()
                            })
                })
                .addOnSuccessListener { s -> acceptanceData.setValue(Acceptance.SUCCESS) }
                .addOnFailureListener { e -> acceptanceData.setValue(Acceptance.ERROR) }
        return acceptanceData
    }

    private fun toUserModel(doc: DocumentSnapshot): UserModel {
        return UserModel(
                doc.id,
                doc.getString(UsersData.FIELD_USERNAME),
                doc.getString(UsersData.FIELD_EMAIL),
                doc.getString(UsersData.FIELD_PHOTO_URL)
        )
    }

    private fun getIds(meetingId: String, subcollection: String): LiveData<List<String>> {
        val idsData = MutableLiveData<List<String>>()
        firestore.collection(MeetingsMetadata.COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(subcollection)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val ids = queryDocumentSnapshots.documents
                            .map { it.getString(MeetingsMetadata.Users.FIELD_ID)!! }
                    idsData.setValue(ids)
                }
                .addOnFailureListener { e -> idsData.setValue(null) }
        return idsData
    }

    private fun getUser(id: String): LiveData<UserModel> {
        val idsData = MutableLiveData<UserModel>()
        firestore.collection(UsersData.COLLECTION_USERS)
                .document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userModel = toUserModel(documentSnapshot)
                    idsData.setValue(userModel)
                }
                .addOnFailureListener { e -> idsData.setValue(null) }
        return idsData
    }

    private fun getUsers(ids: List<String>): LiveData<List<UserModel>> {
        val userModelLiveDatas = ArrayList<LiveData<UserModel>>()
        for (id in ids) {
            userModelLiveDatas.add(getUser(id))
        }
        return LiveDataUtils.zip(userModelLiveDatas)
    }

    private fun <T : LoadUsersStatus.Success> getUsers(
            meetingId: String,
            subcollection: String,
            successMapper: Function<List<UserModel>, T>
    ): LiveData<LoadUsersStatus> {
        val usersData = MutableLiveData<LoadUsersStatus>()
        usersData.value = LoadUsersStatus.Progress()
        Transformations
                .switchMap(getIds(meetingId, subcollection)) { this.getUsers(it) }
                .observeForever { userModels ->
                    usersData.setValue(successMapper.apply(userModels))
                }
        return usersData
    }
}
