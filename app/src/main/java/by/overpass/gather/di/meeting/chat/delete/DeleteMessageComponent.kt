package by.overpass.gather.di.meeting.chat.delete

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.dialog.delete.DeleteDialogFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [DeleteMessageModule::class])
interface DeleteMessageComponent {

    fun inject(deleteDialogFragment: DeleteDialogFragment)
}