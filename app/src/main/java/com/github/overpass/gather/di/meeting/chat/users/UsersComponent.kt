package com.github.overpass.gather.di.meeting.chat.users

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.meeting.chat.users.UsersActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [UsersModule::class])
interface UsersComponent {

    fun inject(usersActivity: UsersActivity)
}