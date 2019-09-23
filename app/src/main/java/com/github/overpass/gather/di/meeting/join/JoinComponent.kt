package com.github.overpass.gather.di.meeting.join

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.meeting.join.JoinFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [JoinModule::class])
interface JoinComponent {

    fun inject(joinFragment: JoinFragment)
}