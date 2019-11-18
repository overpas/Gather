package by.overpass.gather.ui.meeting.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import by.overpass.gather.data.repo.message.Message
import by.overpass.gather.model.usecase.meeting.MeetingUseCase
import by.overpass.gather.model.usecase.message.MessagesUseCase
import by.overpass.gather.model.usecase.userdata.RoleUseCase
import by.overpass.gather.ui.map.AuthUser
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus
import javax.inject.Inject

class ChatViewModel @Inject constructor(
        private val meetingUseCase: MeetingUseCase,
        private val roleUseCase: RoleUseCase,
        private val messagesUseCase: MessagesUseCase,
        private val selectedItemsData: MutableLiveData<Int>,
        private val meetingId: String
) : ViewModel() {

    val currentUser: LiveData<AuthUser>
        get() = messagesUseCase.currentUser

    fun messages(): LiveData<MessageModel> {
        return Transformations.map(messagesUseCase.messages(meetingId)) { status ->
            if (status.tag() == MessageStatus.ERROR) {
                return@map MessageModel.Error(status.`as`<MessageStatus.Error>(MessageStatus.Error::class.java))
            } else {
                val messages = status.`as`(MessageStatus.Success::class.java).messages
                return@map MessageModel.Success(mapMessages(messages))
            }
        }
    }

    private fun mapMessages(messages: List<Message>): List<IMessageImpl> {
        return messages.map { message ->
                    IMessageImpl(
                            message.id,
                            message.text,
                            IUserImpl(
                                    message.authorId,
                                    message.authorName,
                                    message.authorPhotoUrl
                            ),
                            message.date
                    )
                }
    }

    fun send(input: String) {
        messagesUseCase.send(meetingId, input)
    }

    fun checkUserRole(): LiveData<AuthUser.Role> {
        return roleUseCase.getCurrentUserRole(meetingId)
    }

    fun setSelection(count: Int) {
        selectedItemsData.value = count
    }

    fun selectedItems(): LiveData<Int> {
        return selectedItemsData
    }

    fun delete(ids: List<String>): LiveData<DeleteStatus> {
        return messagesUseCase.delete(meetingId, ids)
    }

    fun loadMeeting(): LiveData<LoadMeetingStatus> {
        return meetingUseCase.loadMeeting(meetingId)
    }
}
