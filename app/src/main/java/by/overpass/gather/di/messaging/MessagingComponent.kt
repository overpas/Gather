package by.overpass.gather.di.messaging

import by.overpass.gather.di.ViewScope
import by.overpass.gather.data.messaging.MyFirebaseMessagingService
import dagger.Subcomponent

@ViewScope
@Subcomponent
interface MessagingComponent {

    fun inject(messagingService: MyFirebaseMessagingService)
}