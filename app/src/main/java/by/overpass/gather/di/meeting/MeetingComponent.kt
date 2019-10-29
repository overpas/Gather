package by.overpass.gather.di.meeting

import by.overpass.gather.di.meeting.chat.ChatComponent
import by.overpass.gather.di.meeting.join.JoinComponent
import by.overpass.gather.ui.meeting.MeetingActivity
import dagger.BindsInstance
import dagger.Subcomponent

@MeetingScope
@Subcomponent(modules = [MeetingModule::class])
interface MeetingComponent {

    fun getChatComponent(): ChatComponent

    fun getJoinComponent(): JoinComponent

    fun inject(meetingActivity: MeetingActivity)

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance meetingId: String): MeetingComponent
    }
}