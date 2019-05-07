package com.github.overpass.gather.model.data.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.map.MapActivity;
import com.github.overpass.gather.screen.meeting.MeetingActivity;
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String type = data.get("type");
        if (type != null) {
            handleMessage(type, data);
        }
    }

    private void handleMessage(@NonNull String type, Map<String, String> data) {
        if (type.equals(MessageType.USER_ENROLLED.getType())) {
            showNotification(MessageType.USER_ENROLLED, data);
        } else if (type.equals(MessageType.ACCEPTED.getType())) {
            showNotification(MessageType.ACCEPTED, data);
        }
    }

    private void showNotification(MessageType messageType, Map<String, String> data) {
        String meetingId = data.get("meeting_id");
        if (meetingId == null) {
            return;
        }
        switch (messageType) {
            case USER_ENROLLED:
                showEnrolled(meetingId);
                break;
            case ACCEPTED:
                showAccepted(meetingId);
                break;
        }
    }

    private void showEnrolled(String meetingId) {
        Intent first = new Intent(this, MeetingActivity.class);
        first.putExtra(MEETING_ID_KEY, meetingId);
        Intent second = new Intent(this, UsersActivity.class);
        second.putExtra(MEETING_ID_KEY, meetingId);
        showMeetingNotification(R.integer.notification_user_enrolled_id,
                R.string.notification_user_enrolled, first, second);
    }

    private void showAccepted(String meetingId) {
        Intent first = new Intent(this, MapActivity.class);
        Intent second = new Intent(this, MeetingActivity.class);
        second.putExtra(MEETING_ID_KEY, meetingId);
        showMeetingNotification(R.integer.notification_user_accepted_id,
                R.string.notification_user_accepted, first, second);
    }

    private void showMeetingNotification(@IntegerRes int idRes,
                                         @StringRes int textRes, Intent... intents) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        for (Intent intent : intents) {
            taskStackBuilder.addNextIntent(intent);
        }
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int id = getResources().getInteger(idRes);
        String notificationText = getString(textRes);
        showNotification(id, notificationText, pendingIntent);
    }

    private void showNotification(int notificationId, String text, PendingIntent pendingIntent) {
        createNotificationChannel();
        String channelId = getString(R.string.channel_id);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_add_marker)
                .setContentTitle(getString(R.string.gather_app_name))
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat.from(this).notify(notificationId, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelId = getString(R.string.channel_id);
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
