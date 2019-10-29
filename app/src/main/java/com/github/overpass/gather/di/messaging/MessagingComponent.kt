package com.github.overpass.gather.di.messaging

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.model.data.messaging.MyFirebaseMessagingService
import dagger.Subcomponent

@ViewScope
@Subcomponent
interface MessagingComponent {

    fun inject(messagingService: MyFirebaseMessagingService)
}