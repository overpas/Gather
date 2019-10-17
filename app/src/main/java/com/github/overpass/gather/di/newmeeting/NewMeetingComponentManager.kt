package com.github.overpass.gather.di.newmeeting

import com.github.overpass.gather.di.ComponentManager

class NewMeetingComponentManager(
        newMeetingComponent: NewMeetingComponent
) : ComponentManager<Unit, NewMeetingComponent>({ newMeetingComponent })