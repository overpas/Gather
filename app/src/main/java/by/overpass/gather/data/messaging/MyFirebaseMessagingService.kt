package by.overpass.gather.data.messaging

import android.util.Log
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.commons.abstractions.Result
import by.overpass.gather.commons.notifications.NotificationsManager
import by.overpass.gather.data.repo.notifications.NotificationParser
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationsManager: NotificationsManager
    @Inject
    lateinit var notificationParser: NotificationParser

    init {
        componentManager.getMessagingComponent()
                .inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        when (val result = notificationParser.parseNotification(remoteMessage.data)) {
            is Result.Success -> notificationsManager.showNotification(result.data)
            is Result.Error -> Log.e(TAG, result.exception.toString())
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingSer"
    }
}
