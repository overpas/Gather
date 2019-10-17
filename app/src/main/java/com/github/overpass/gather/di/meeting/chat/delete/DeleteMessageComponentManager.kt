package com.github.overpass.gather.di.meeting.chat.delete

import com.github.overpass.gather.di.ComponentManager

class DeleteMessageComponentManager(
        deleteMessageComponent: DeleteMessageComponent
) : ComponentManager<Unit, DeleteMessageComponent>({deleteMessageComponent})