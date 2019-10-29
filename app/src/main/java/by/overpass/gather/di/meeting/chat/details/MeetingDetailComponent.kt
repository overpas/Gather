package by.overpass.gather.di.meeting.chat.details

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.dialog.details.MeetingDetailsDialogFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [MeetingDetailModule::class])
interface MeetingDetailComponent {

    fun inject(meetingDetailsDialogFragment: MeetingDetailsDialogFragment)
}