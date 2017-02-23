package com.example.twistedpurpose.lab8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeZoneChangeReceiver extends BroadcastReceiver {
    public TimeZoneChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MusicService.class));
    }
}
