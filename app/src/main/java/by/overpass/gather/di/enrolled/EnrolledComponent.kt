package by.overpass.gather.di.enrolled

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.meeting.enrolled.EnrolledActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [EnrolledModule::class])
interface EnrolledComponent {

    fun inject(enrolledActivity: EnrolledActivity)
}