package com.github.overpass.gather.di.newmeeting

import com.github.overpass.gather.di.ComponentManager

class NewMeetingComponentManager(
        creator: () -> NewMeetingComponent
) : ComponentManager<NewMeetingComponent>(creator)