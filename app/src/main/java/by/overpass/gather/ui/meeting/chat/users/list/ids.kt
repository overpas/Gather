package by.overpass.gather.ui.meeting.chat.users.list

import by.overpass.gather.ui.meeting.chat.users.model.UserModel

fun ids(users: List<UserModel>): Array<String> = users
        .map { it.id }
        .toTypedArray()