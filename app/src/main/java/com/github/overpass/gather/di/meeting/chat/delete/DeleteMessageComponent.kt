package com.github.overpass.gather.di.meeting.chat.delete

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.dialog.delete.DeleteDialogFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [DeleteMessageModule::class])
interface DeleteMessageComponent {

    fun inject(deleteDialogFragment: DeleteDialogFragment)
}