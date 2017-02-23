package com.example.twistedpurpose.lab8;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Handles notification and event of changing time zones
 * Will display notification of a time zone change and
 * plays some sick beats
 */
public class TimeZoneChangeReceiver extends BroadcastReceiver {

    // Notification key for song service
    private static final int SONG_NOTIFICATION = 1;

    public TimeZoneChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Build a notification for when time zone changes
        Notification.Builder builder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(context.getString(R.string.time_zone_notification_title))
                        .setContentText(context.getString(R.string.time_zone_notification_message));

        //Tie it to the notification services
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Start up MusicPlayer activity
        Intent resultIntent = new Intent(context, MusicPlayer.class);

        //Add music player to the task stack builder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MusicPlayer.class);
        stackBuilder.addNextIntent(resultIntent);

        // Flag the pending intent
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        // Notify user of playing song and time zone change
        nm.notify(SONG_NOTIFICATION, builder.build());

        // Start playing some sick beats
        context.startService(new Intent(context, MusicService.class));
    }
}
