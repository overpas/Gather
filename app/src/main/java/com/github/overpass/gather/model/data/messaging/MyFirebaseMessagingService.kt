package com.github.overpass.gather.model.data.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.map.MapActivity
import com.github.overpass.gather.screen.meeting.MeetingActivity
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val type = data["type"]
        if (type != null) {
            handleMessage(type, data)
        }
    }

    private fun handleMessage(type: String, data: Map<String, String>) {
        if (type == MessageType.USER_ENROLLED.type) {
            showNotification(MessageType.USER_ENROLLED, data)
        } else if (type == MessageType.ACCEPTED.type) {
            showNotification(MessageType.ACCEPTED, data)
        }
    }

    private fun showNotification(messageType: MessageType, data: Map<String, String>) {
        val meetingId = data["meeting_id"] ?: return
        when (messageType) {
            MessageType.USER_ENROLLED -> showEnrolled(meetingId)
            MessageType.ACCEPTED -> showAccepted(meetingId)
        }
    }

    private fun showEnrolled(meetingId: String) {
        val first = Intent(this, MeetingActivity::class.java)
        first.putExtra(MEETING_ID_KEY, meetingId)
        val second = Intent(this, UsersActivity::class.java)
        second.putExtra(MEETING_ID_KEY, meetingId)
        showMeetingNotification(R.integer.notification_user_enrolled_id,
                R.string.notification_user_enrolled, first, second)
    }

    private fun showAccepted(meetingId: String) {
        val first = Intent(this, MapActivity::class.java)
        val second = Intent(this, MeetingActivity::class.java)
        second.putExtra(MEETING_ID_KEY, meetingId)
        showMeetingNotification(R.integer.notification_user_accepted_id,
                R.string.notification_user_accepted, first, second)
    }

    private fun showMeetingNotification(@IntegerRes idRes: Int,
                                        @StringRes textRes: Int, vararg intents: Intent) {
        val taskStackBuilder = TaskStackBuilder.create(this)
        for (intent in intents) {
            taskStackBuilder.addNextIntent(intent)
        }
        val pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val id = resources.getInteger(idRes)
        val notificationText = getString(textRes)
        showNotification(id, notificationText, pendingIntent)
    }

    private fun showNotification(notificationId: Int, text: String, pendingIntent: PendingIntent?) {
        createNotificationChannel()
        val channelId = getString(R.string.channel_id)
        val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_add_marker)
                .setContentTitle(getString(R.string.gather_app_name))
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .setAutoCancel(true)
                .build()
        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = getString(R.string.channel_id)
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    companion object {

        private const val MEETING_ID_KEY = "MEETING_ID_KEY"
    }
}
