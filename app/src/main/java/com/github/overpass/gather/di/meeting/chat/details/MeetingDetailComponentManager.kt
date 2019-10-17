package com.github.overpass.gather.di.meeting.chat.details

import com.github.overpass.gather.di.ComponentManager

class MeetingDetailComponentManager(
        meetingDetailComponent: MeetingDetailComponent
) : ComponentManager<Unit, MeetingDetailComponent>({ meetingDetailComponent })