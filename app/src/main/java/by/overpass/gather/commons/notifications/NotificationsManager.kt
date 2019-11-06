package by.overpass.gather.commons.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import by.overpass.gather.R
import by.overpass.gather.data.repo.notifications.NotificationResource
import javax.inject.Inject

class NotificationsManager @Inject constructor(
        private val context: Context
) {

    fun showNotification(notificationResource: NotificationResource) {
        createNotificationChannel()
        NotificationManagerCompat.from(context)
                .notify(
                        notificationResource.id,
                        notificationResource.notification
                )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = context.getString(R.string.channel_id)
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}