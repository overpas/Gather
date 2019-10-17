package com.github.overpass.gather.di.meeting.join

import com.github.overpass.gather.di.ComponentManager

class JoinComponentManager(
        joinComponent: JoinComponent
) : ComponentManager<Unit, JoinComponent>({ joinComponent })