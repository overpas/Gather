package by.overpass.gather.di.meeting

import android.util.Log
import androidx.lifecycle.Lifecycle
import by.overpass.gather.di.meeting.chat.ChatComponentManager
import by.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import by.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import by.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import by.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import by.overpass.gather.di.meeting.chat.users.UsersComponent
import by.overpass.gather.di.meeting.join.JoinComponent
import by.overpass.gather.commons.android.lifecycle.LifecycleDisposable

class MeetingComponentManager(
        private val meetingComponentFactory: MeetingComponent.Factory
) : MeetingComponent.Factory {

    init {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    private lateinit var meetingComponent: MeetingComponent
    private lateinit var chatComponentManagerDisposable: LifecycleDisposable<ChatComponentManager>

    override fun create(meetingId: String): MeetingComponent {
        meetingComponent = meetingComponentFactory.create(meetingId)
        return meetingComponent
    }

    fun getChatComponent(lifecycle: Lifecycle): ChatComponentManager {
        chatComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                ChatComponentManager(meetingComponent.getChatComponent())
        )
        return chatComponentManagerDisposable.value!!
    }

    fun getJoinComponent(): JoinComponent = meetingComponent.getJoinComponent()

    fun getUsersComponent(): UsersComponent =
            chatComponentManagerDisposable.value!!.getUsersComponent()

    fun getMeetingDetailComponent(): MeetingDetailComponent =
            chatComponentManagerDisposable.value!!.getMeetingDetailsComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            chatComponentManagerDisposable.value!!.getDeleteMessageComponent()

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentsComponent(lifecycle)

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentDetailComponent()

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Destroyed")
    }
}