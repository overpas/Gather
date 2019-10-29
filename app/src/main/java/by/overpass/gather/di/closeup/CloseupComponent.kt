package by.overpass.gather.di.closeup

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.meeting.chat.attachments.closeup.CloseupActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [CloseupModule::class])
interface CloseupComponent {

    fun inject(closeupActivity: CloseupActivity)
}