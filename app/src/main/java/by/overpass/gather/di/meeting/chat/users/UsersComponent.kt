package by.overpass.gather.di.meeting.chat.users

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.meeting.chat.users.UsersActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [UsersModule::class])
interface UsersComponent {

    fun inject(usersActivity: UsersActivity)
}