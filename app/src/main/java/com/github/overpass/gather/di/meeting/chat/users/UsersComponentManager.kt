package com.github.overpass.gather.di.meeting.chat.users

import com.github.overpass.gather.di.ComponentManager

class UsersComponentManager(
        usersComponent: UsersComponent
) : ComponentManager<Unit, UsersComponent>({ usersComponent })