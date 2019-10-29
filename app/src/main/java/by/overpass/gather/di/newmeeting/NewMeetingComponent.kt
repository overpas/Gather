package by.overpass.gather.di.newmeeting

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.create.NewMeetingActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [NewMeetingModule::class])
interface NewMeetingComponent {

    fun inject(newMeetingActivity: NewMeetingActivity)
}