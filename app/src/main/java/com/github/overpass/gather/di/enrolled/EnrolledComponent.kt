package com.github.overpass.gather.di.enrolled

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.meeting.enrolled.EnrolledActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [EnrolledModule::class])
interface EnrolledComponent {

    fun inject(enrolledActivity: EnrolledActivity)
}