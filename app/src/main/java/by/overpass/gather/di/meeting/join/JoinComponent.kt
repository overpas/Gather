package by.overpass.gather.di.meeting.join

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.meeting.join.JoinFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [JoinModule::class])
interface JoinComponent {

    fun inject(joinFragment: JoinFragment)
}