package com.github.overpass.gather.model.repo.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.abstractions.Result
import com.github.overpass.gather.model.data.messaging.MessageType
import com.github.overpass.gather.ui.map.MapActivity
import com.github.overpass.gather.ui.meeting.MeetingActivity
import com.github.overpass.gather.ui.meeting.chat.users.UsersActivity
import javax.inject.Inject

class NotificationParser @Inject constructor(
        private val context: Context
) {

    fun parseNotification(data: Map<String, String>): Result<NotificationResource> = data[TYPE_KEY]
            ?.let { parseByType(it, data) }
            ?: Result.Error(IllegalArgumentException("Couldn't parse the notification"))

    private fun parseByType(type: String, data: Map<String, String>): Result<NotificationResource> {
        val errorResult = Result.Error(IllegalArgumentException("Cannot create a notification for type $type"))
        val meetingId = data[MEETING_ID_KEY] ?: return errorResult
        return when (type) {
            MessageType.ACCEPTED.type -> Result.Success(newUserAccepted(meetingId))
            MessageType.USER_ENROLLED.type -> Result.Success(newUserEnrolled(meetingId))
            else -> errorResult
        }
    }

    private fun newUserEnrolled(meetingId: String): NotificationResource {
        val first = Intent(context, MeetingActivity::class.java)
        first.putExtra(MEETING_ID_EXTRA_KEY, meetingId)
        val second = Intent(context, UsersActivity::class.java)
        second.putExtra(MEETING_ID_EXTRA_KEY, meetingId)
        val taskStackBuilder = TaskStackBuilder.create(context)
        for (intent in arrayOf(first, second)) {
            taskStackBuilder.addNextIntent(intent)
        }
        val pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val id = context.resources.getInteger(R.integer.notification_user_enrolled_id)
        val notificationText = context.getString(R.string.notification_user_enrolled)
        val channelId = context.getString(R.string.channel_id)
        val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_add_marker)
                .setContentTitle(context.getString(R.string.gather_app_name))
                .setContentIntent(pendingIntent)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .build()
        return NotificationResource(id, notification)
    }

    private fun newUserAccepted(meetingId: String): NotificationResource {
        val first = Intent(context, MapActivity::class.java)
        val second = Intent(context, MeetingActivity::class.java)
        second.putExtra(MEETING_ID_EXTRA_KEY, meetingId)
        val taskStackBuilder = TaskStackBuilder.create(context)
        for (intent in arrayOf(first, second)) {
            taskStackBuilder.addNextIntent(intent)
        }
        val pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val id = context.resources.getInteger(R.integer.notification_user_accepted_id)
        val notificationText = context.getString(R.string.notification_user_accepted)
        val channelId = context.getString(R.string.channel_id)
        val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_add_marker)
                .setContentTitle(context.getString(R.string.gather_app_name))
                .setContentIntent(pendingIntent)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .build()
        return NotificationResource(id, notification)
    }

    companion object {

        private const val MEETING_ID_EXTRA_KEY = "MEETING_ID_KEY"
        private const val MEETING_ID_KEY = "meeting_id"
        private const val TYPE_KEY = "type"
    }
}